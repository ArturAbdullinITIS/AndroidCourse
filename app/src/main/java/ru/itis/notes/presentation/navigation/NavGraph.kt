package ru.itis.notes.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.notes.presentation.screens.first.RegistrationScreen
import ru.itis.notes.presentation.screens.second.NotesScreen
import ru.itis.notes.presentation.screens.third.CreateNoteScreen
import ru.itis.practice.R


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val regRoute = remember { context.getString(R.string.route_registration) }
    val notesRoutePattern = remember { context.getString(R.string.route_notes_pattern) }
    val createRoute = remember { context.getString(R.string.route_create_note) }
    val emailArgKey = remember { context.getString(R.string.arg_email) }

    NavHost(
        navController = navController,
        startDestination = regRoute
    ) {
        composable(regRoute) {
            RegistrationScreen(
                onButtonClick = { email ->
                    val route = notesRoutePattern.replace("{email}", email)
                    navController.navigate(route)
                }
            )
        }

        composable(notesRoutePattern) {
            val email = it.arguments?.getString(emailArgKey).toString()
            NotesScreen(
                email = email,
                onCreateNoteButton = {
                    navController.navigate(createRoute)
                }
            )
        }
        composable(createRoute) {
            CreateNoteScreen(
                onSaveButtonClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}


sealed class Screen(val route: String) {
    data object RegistrationScreen: Screen("reg")
    data object NotesScreen: Screen("notes?email={email}") {
        fun createRoute(email: String): String {
            return "notes?email=$email"
        }

        fun getEmail(arguments: Bundle?): String {
            return arguments?.getString("email").toString()
        }
    }
    data object CreateNote: Screen("create_note")
}