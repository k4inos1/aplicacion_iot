package com.example.aplicacion_iot.domain.usecase

import com.example.aplicacion_iot.domain.model.SensorState
import com.example.aplicacion_iot.domain.repository.SensorRepository
import javax.inject.Inject

class SendSoilStatusUseCase @Inject constructor(
    private val repository: SensorRepository
) {
    suspend operator fun invoke(status: String) {
        repository.sendSoilStatus(SensorState(status))
    }
}
