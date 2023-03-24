package ru.ninthproject.countrycode.navigation

sealed class Screen(val route:String) {
    object MainScreen: Screen("main_screen")
}