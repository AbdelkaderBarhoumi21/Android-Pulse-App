package com.example.pulse_app.core.network

/** Every relative path and query key in one place. BASE_URL lives in BuildConfig. */
object ApiEndpoints {
    const val TASKS = "tasks"
    const val TASK_BY_ID = "tasks/{id}"
    const val DEVICES = "devices"
    const val DEVICE_BY_ID = "devices/{id}"
    const val PATH_ID = "id"
}