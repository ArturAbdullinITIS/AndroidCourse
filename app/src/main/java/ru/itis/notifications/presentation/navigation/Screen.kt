package ru.itis.notifications.presentation.navigation

object Routes {
    const val SETTINGS = "settings"
    const val EDIT = "edit"
    const val MESSAGES = "messages"
}

sealed class Screen(val route: String){
    object Settings: Screen(Routes.SETTINGS)
    object Edit: Screen(Routes.EDIT)
    object Messages: Screen(Routes.MESSAGES)
}