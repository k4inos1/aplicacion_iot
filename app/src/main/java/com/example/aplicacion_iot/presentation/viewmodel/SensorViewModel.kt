package com.example.aplicacion_iot.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_iot.domain.model.Event
import com.example.aplicacion_iot.domain.model.EventType
import com.example.aplicacion_iot.domain.usecase.GetPumpCommandUseCase
import com.example.aplicacion_iot.domain.usecase.SendSoilStatusUseCase
import com.example.aplicacion_iot.domain.usecase.UpdatePumpCommandUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SensorViewModel @Inject constructor(
    private val sendSoilStatusUseCase: SendSoilStatusUseCase,
    private val getPumpCommandUseCase: GetPumpCommandUseCase,
    private val updatePumpCommandUseCase: UpdatePumpCommandUseCase
) : ViewModel() {

    private val _pumpState = MutableLiveData<String>()
    val pumpState: LiveData<String> = _pumpState

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var irrigationJob: Job? = null

    init {
        addEvent("ViewModel inicializado. Listo para operar.", EventType.INFO)
        startFetchingPumpCommands()
    }

    fun onStatusChanged(isWet: Boolean) {
        viewModelScope.launch {
            val status = if (isWet) "Húmedo" else "Seco"
            try {
                addEvent("Enviando estado del suelo: $status", EventType.UPLOAD)
                sendSoilStatusUseCase(status)
                addEvent("Estado enviado correctamente: $status", EventType.UPLOAD)
            } catch (e: Exception) {
                addEvent("Error al enviar estado: ${e.message}", EventType.ERROR)
            }
        }
    }

    fun startManualIrrigation(durationSeconds: Int) {
        irrigationJob?.cancel() // Cancel any previous irrigation job
        irrigationJob = viewModelScope.launch {
            _isLoading.value = true
            try {
                addEvent("Iniciando riego manual por $durationSeconds segundos...", EventType.INFO)
                updatePumpCommandUseCase("ENCENDER")
                _pumpState.postValue("ENCENDER")

                delay(durationSeconds * 1000L)

                addEvent("Riego manual completado.", EventType.INFO)
                updatePumpCommandUseCase("APAGAR")
                _pumpState.postValue("APAGAR")

            } catch (e: Exception) {
                addEvent("Error durante el riego manual: ${e.message}", EventType.ERROR)
                // Ensure pump is turned off in case of error
                updatePumpCommandUseCase("APAGAR")
                _pumpState.postValue("APAGAR")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearHistory() {
        _events.value = emptyList()
        addEvent("Historial limpiado.", EventType.INFO)
    }

    private fun startFetchingPumpCommands() {
        viewModelScope.launch {
            while (true) {
                if (irrigationJob?.isActive != true) { // Don't fetch while manual irrigation is active
                    try {
                        val newPumpState = getPumpCommandUseCase()
                        if (_pumpState.value != newPumpState.command) {
                            _pumpState.postValue(newPumpState.command)
                            addEvent("Estado de la bomba actualizado desde el servidor: ${newPumpState.command}", EventType.DOWNLOAD)
                        }
                    } catch (e: Exception) {
                        val errorMessage = e.message ?: "Error desconocido"
                        addEvent("Error al obtener comando: $errorMessage", EventType.ERROR)
                        _pumpState.postValue("Error de red")
                    }
                }
                delay(5000) // Consulta cada 5 segundos
            }
        }
    }

    private fun addEvent(message: String, type: EventType) {
        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val newEvent = Event(message, timestamp, type)
        _events.update { currentEvents ->
            listOf(newEvent) + currentEvents
        }
    }
}
