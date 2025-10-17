package com.example.aplicacion_iot.domain.model

import com.google.gson.annotations.SerializedName

data class PumpState(@SerializedName("comando") val command: String)
