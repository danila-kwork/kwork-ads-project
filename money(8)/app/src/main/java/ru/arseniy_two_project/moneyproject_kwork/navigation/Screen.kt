package ru.arseniy_two_project.moneyproject_kwork.navigation

sealed class Screen(val route:String) {
    object Auth: Screen("auth_screen")
    object Main: Screen("main_screen")
    object WithdrawalRequests: Screen("withdrawal_requests_screen")
    object Settings: Screen("settings_screen")
}