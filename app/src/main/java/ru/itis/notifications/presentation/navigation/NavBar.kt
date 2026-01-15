package ru.itis.notifications.presentation.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.itis.notifications.R
import ru.itis.notifications.presentation.screens.first.NotificationSettingsScreen
import ru.itis.notifications.presentation.screens.second.NotificationEditScreen
import ru.itis.notifications.presentation.screens.third.UserMessagesScreen


@Composable
fun NavBar() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Settings.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Settings.route) {
                NotificationSettingsScreen()
            }
            composable(Screen.Edit.route) {
                NotificationEditScreen()
            }
            composable(Screen.Messages.route) {
                UserMessagesScreen()
            }
        }
    }

}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val screens = listOf(
        Screen.Settings,
        Screen.Edit,
        Screen.Messages
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = when (screen) {
                            Screen.Settings -> Icons.Default.Settings
                            Screen.Edit -> Icons.Default.Edit
                            Screen.Messages -> Icons.Default.AddComment
                        },
                        contentDescription = getScreenTitle(screen)
                    )
                },
                label = { Text(getScreenTitle(screen))},
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun getScreenTitle(screen: Screen): String {
    return when (screen) {
        Screen.Settings -> stringResource(R.string.nav_settings)
        Screen.Edit -> stringResource(R.string.nav_edit)
        Screen.Messages -> stringResource(R.string.nav_messages)
    }
}