package ru.fourthproject.guessthenumber.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
    object AuthScreen: Screen("auth_screen")
    object SettingsScreen: Screen("settings_screen")
}