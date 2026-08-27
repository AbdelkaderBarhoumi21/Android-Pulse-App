package com.example.pulse_app.core.routing

sealed class Routes(
    val route: String,
) {
    data object TaskList : Routes("tasks")

    data object TaskDetail : Routes("tasks/{taskId}") {
        fun build(taskId: String) = "tasks/$taskId"

        const val ARG = "taskId"
    }

    data object TaskCreate : Routes("tasks/create")

    data object TaskEdit : Routes("tasks/{taskId}/edit") {
        fun build(taskId: String) = "tasks/$taskId/edit"

        const val ARG = "taskId"
    }
}
