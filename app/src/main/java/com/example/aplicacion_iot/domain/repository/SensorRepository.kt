package com.example.aplicacion_iot.domain.repository

import com.example.aplicacion_iot.domain.model.PumpState
import com.example.aplicacion_iot.domain.model.SensorState

interface SensorRepository {

    suspend fun sendSoilStatus(sensorState: SensorState)

    suspend fun getPumpCommand(): PumpState

    suspend fun updatePumpCommand(pumpState: PumpState)
}
