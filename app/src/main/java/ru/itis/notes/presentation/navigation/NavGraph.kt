package ru.itis.notes.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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

    val regRoute = stringResource(R.string.route_registration)
    val notesRoutePattern = stringResource(R.string.route_notes_pattern)
    val createRoute = stringResource(R.string.route_create_note)
    val emailArgKey = stringResource(R.string.arg_email)

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

        composable(notesRoutePattern) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(emailArgKey).toString()
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
