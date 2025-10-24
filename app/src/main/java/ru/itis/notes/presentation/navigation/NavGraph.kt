package ru.itis.notes.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavArgument
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.notes.presentation.screens.first.RegistrationScreen
import ru.itis.notes.presentation.screens.second.NotesScreen


@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.RegistrationScreen.route
    ) {
        composable(Screen.RegistrationScreen.route) {
            RegistrationScreen(
                onButtonClick = { email -> navController.navigate(Screen.NotesScreen.createRoute(email)) }
            )
        }

        composable(Screen.NotesScreen.route) {
            val email = Screen.NotesScreen.getEmail(it.arguments)
            NotesScreen(
                email = email,
                modifier = Modifier
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