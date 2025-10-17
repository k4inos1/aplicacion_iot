package com.example.aplicacion_iot.data.remote

import com.example.aplicacion_iot.domain.model.PumpState
import com.example.aplicacion_iot.domain.model.SensorState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ApiService {

    @PUT("soil")
    suspend fun sendSoilStatus(@Body sensorState: SensorState): Response<Unit>

    @GET("pump")
    suspend fun getPumpCommand(): PumpState

    @PUT("pump")
    suspend fun updatePumpCommand(@Body pumpState: PumpState): Response<Unit>
}
