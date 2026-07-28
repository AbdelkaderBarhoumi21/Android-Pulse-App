package com.example.pulse_app.features.task.domain.model
/** Wire value is lowercase: low | medium | high. */
enum class TaskPriority {
    LOW,MEDIUM,HIGH;
    val wire:String get() = name.lowercase()
    companion object{
        fun fromApi(value:String) : TaskPriority =
            entries.firstOrNull{
                item -> item.wire== value.lowercase()
            } ?: LOW

    }
}