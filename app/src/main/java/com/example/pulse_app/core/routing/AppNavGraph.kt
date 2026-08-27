package com.example.pulse_app.core.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pulse_app.features.task.presentation.screens.TaskListScreen

@Composable
fun AppNavGraph(startTaskId: String? = null) {
    val nav = rememberNavController()
    NavHost(
        navController = nav,
        startDestination = Routes.TaskList.route,
    ) {
        composable(Routes.TaskList.route) {
            TaskListScreen(
                onTaskClick = { nav.navigate(Routes.TaskDetail.build(it)) },
                onAddClick = { nav.navigate(Routes.TaskCreate.route) },
            )
        }

        composable(
            Routes.TaskDetail.route,
            arguments = listOf(navArgument(Routes.TaskDetail.ARG) { type = NavType.StringType }),
        ) { /* TaskDetailScreen(taskId, onEdit, onBack) */ }

        composable(Routes.TaskCreate.route) { /* TaskEditScreen(mode = Create, onDone = { nav.popBackStack() }) */ }

        composable(
            Routes.TaskEdit.route,
            arguments = listOf(navArgument(Routes.TaskEdit.ARG) { type = NavType.StringType }),
        ) { /* TaskEditScreen(mode = Edit, onDone = { nav.popBackStack() }) */ }
    }

    // deep link from a tapped notification
    LaunchedEffect(startTaskId) {
        startTaskId?.let {
            nav.navigate(Routes.TaskDetail.build(it))
        }
    }
}
