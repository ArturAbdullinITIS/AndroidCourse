package ru.itis.practice.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.itis.practice.presentation.screen.create.CreateScreen
import ru.itis.practice.presentation.screen.login.LoginScreen
import ru.itis.practice.presentation.screen.mainscreen.MainScreen
import ru.itis.practice.presentation.screen.profile.ProfileScreen
import ru.itis.practice.presentation.screen.recovery.RecoveryScreen
import ru.itis.practice.presentation.screen.register.RegisterScreen

@Composable
fun NavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Register.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = tween(300)
                )
            }
        ) {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = Screen.Login.route,
            enterTransition = {
                val from = initialState.destination.route
                when (from) {
                    Screen.Register.route -> {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                    Screen.Main.route,
                    Screen.Profile.route -> {
                        slideInHorizontally(
                            initialOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                    else -> {
                        slideInHorizontally(
                            initialOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                }
            },
            exitTransition = {
                val to = targetState.destination.route
                when (to) {
                    Screen.Register.route -> {
                        slideOutHorizontally(
                            targetOffsetX = { it },
                            animationSpec = tween(300)
                        )
                    }
                    Screen.Main.route -> {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                    else -> {
                        slideOutHorizontally(
                            targetOffsetX = { -it },
                            animationSpec = tween(300)
                        )
                    }
                }
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            }
        ) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToRecovery = { email ->
                    navController.navigate("${NavRoutes.RECOVERY}/$email")
                }
            )
        }


        composable(
            route = Screen.Main.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 0 },
                    animationSpec = tween(0)
                )
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                )
            }
        ) {
            MainScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.Create.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(Screen.Create.route) {
            CreateScreen(
                onNavigateToMain = {
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = {
                slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = tween(300)
                )
            },
            popExitTransition = {
                slideOutVertically(
                    targetOffsetY = { -it },
                    animationSpec = tween(300)
                )
            }
        ) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToLogIn = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(
            route = "${NavRoutes.RECOVERY}/{${NavRoutes.RECOVERY_EMAIL_ARG}}",
            arguments = listOf(
                navArgument(NavRoutes.RECOVERY_EMAIL_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString(NavRoutes.RECOVERY_EMAIL_ARG) ?: ""
            RecoveryScreen(
                email = email,
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
