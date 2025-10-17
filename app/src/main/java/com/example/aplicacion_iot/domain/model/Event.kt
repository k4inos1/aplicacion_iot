package com.example.aplicacion_iot.domain.model

/**
 * Represents a single log event in the event history.
 */
data class Event(
    val message: String,
    val timestamp: String,
    val type: EventType
)

enum class EventType {
    INFO,
    UPLOAD,
    DOWNLOAD,
    ERROR
}
