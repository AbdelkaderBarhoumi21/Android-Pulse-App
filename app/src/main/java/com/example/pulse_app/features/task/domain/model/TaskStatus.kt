package com.example.pulse_app.features.task.domain.model

/** Wire is camelCase: pending | inProgress | completed (Postgres stores snake_case). */
enum class TaskStatus(val wire: String) {
    PENDING("pending"), IN_PROGRESS("inProgress"), COMPLETED("completed");
    companion object {
        fun fromWire(value: String): TaskStatus =
            entries.firstOrNull { it.wire == value } ?: PENDING
    }
}