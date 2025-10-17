package com.example.aplicacion_iot.domain.usecase

import com.example.aplicacion_iot.domain.model.PumpState
import com.example.aplicacion_iot.domain.repository.SensorRepository
import javax.inject.Inject

class GetPumpCommandUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend operator fun invoke(): PumpState {
        return repository.getPumpCommand()
    }
}
