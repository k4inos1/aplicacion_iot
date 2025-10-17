package com.example.aplicacion_iot.data.repository

import com.example.aplicacion_iot.data.remote.ApiService
import com.example.aplicacion_iot.domain.model.PumpState
import com.example.aplicacion_iot.domain.model.SensorState
import com.example.aplicacion_iot.domain.repository.SensorRepository
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SensorRepository {

    override suspend fun sendSoilStatus(sensorState: SensorState) {
        apiService.sendSoilStatus(sensorState)
    }

    override suspend fun getPumpCommand(): PumpState {
        return apiService.getPumpCommand()
    }

    override suspend fun updatePumpCommand(pumpState: PumpState) {
        apiService.updatePumpCommand(pumpState)
    }
}
