
package ru.itis.practice.presentation.main
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.itis.practice.data.session.SessionDataStore
import ru.itis.practice.data.session.sessionDataStore
import ru.itis.practice.presentation.navigation.NavGraph
import ru.itis.practice.presentation.navigation.Screen
import ru.itis.practice.presentation.screen.profile.ProfileScreen
import ru.itis.practice.presentation.ui.theme.RoomAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isLoggedIn = sessionDataStore.data
                .map { it[SessionDataStore.IS_LOGGED_IN] ?: false }
                .first()

            setContent {
                RoomAppTheme {
                    val navController = rememberNavController()
                    NavGraph(navController)
                    LaunchedEffect(isLoggedIn) {
                        if (isLoggedIn) {
                            navController.navigate(Screen.Main.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    }
                }
            }
        }
    }
}
