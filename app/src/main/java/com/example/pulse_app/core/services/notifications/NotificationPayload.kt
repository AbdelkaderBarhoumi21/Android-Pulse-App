package com.example.pulse_app.core.services.notifications

/** Parsed FCM data payload. Mirrors notification_payload.dart. */
// type.from("task_created")      // → TASK_CREATED
// type.from("something_new")     // → UNKNOWN (pas de crash !)
// Type.from(null)              // → UNKNOWN (pas de crash !)
data class NotificationPayload(
    val type: Type,
    val taskId: String?,
    val title: String,
    val body: String,
) {
    enum class Type {
        TASK_CREATED,
        TASK_UPDATED,
        TASK_DELETED,
        UNKNOWN,
        ;

        companion object {
            fun from(raw: String?) =
                when (raw) {
                    "task_created" -> TASK_CREATED
                    "task_updated" -> TASK_UPDATED
                    "task_deleted" -> TASK_DELETED
                    else -> UNKNOWN
                }
        }
    }

    companion object {
        fun fromData(data: Map<String, String>) =
            NotificationPayload(
                type = Type.from(data["type"]),
                taskId = data["taskId"],
                title = data["title"] ?: "Task update",
                body = data["body"] ?: "",
            )
    }
}
