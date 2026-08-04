package com.example.pulse_app.core.services.notifications

import com.example.pulse_app.core.services.notifications.NotificationPayload
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NotificationPayloadTest {
    @Test
    fun `parses a task_created data message`() {
        val payload =
            NotificationPayload.fromData(
                mapOf("type" to "task_created", "taskId" to "42", "title" to "New task", "body" to "buy milk"),
            )

        assertThat(payload.type).isEqualTo(NotificationPayload.Type.TASK_CREATED)
        assertThat(payload.taskId).isEqualTo("42")
        assertThat(payload.title).isEqualTo("New task")
    }

    @Test
    fun `unknown type and missing fields use safe defaults`() {
        val payload = NotificationPayload.fromData(mapOf("type" to "???"))
        assertThat(payload.type).isEqualTo(NotificationPayload.Type.UNKNOWN)
        assertThat(payload.taskId).isNull()
        assertThat(payload.title).isEqualTo("Task update")
    }
}
