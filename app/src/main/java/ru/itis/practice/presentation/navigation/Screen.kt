package ru.itis.practice.presentation.navigation

sealed class Screen(val route: String) {
    data object Register: Screen(NavRoutes.REGISTER)
    data object Login: Screen(NavRoutes.LOGIN)
    data object Main: Screen(NavRoutes.MAIN)
    data object Create: Screen(NavRoutes.CREATE)
    data object Profile: Screen(NavRoutes.PROFILE)

}