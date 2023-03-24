package ru.cfif31.eventshistory.ui.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
}