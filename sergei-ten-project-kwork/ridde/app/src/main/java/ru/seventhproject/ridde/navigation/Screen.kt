package ru.seventhproject.ridde.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
}