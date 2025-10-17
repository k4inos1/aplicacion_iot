package com.example.aplicacion_iot.domain.usecase

import com.example.aplicacion_iot.domain.repository.SensorRepository
import com.example.aplicacion_iot.domain.model.PumpState
import javax.inject.Inject

class UpdatePumpCommandUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend operator fun invoke(command: String) {
        repository.updatePumpCommand(PumpState(command))
    }
}
