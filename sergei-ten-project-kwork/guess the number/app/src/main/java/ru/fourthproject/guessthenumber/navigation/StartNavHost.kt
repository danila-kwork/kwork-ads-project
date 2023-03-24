package ru.fourthproject.guessthenumber.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.fourthproject.guessthenumber.ui.screens.authScreen.AuthScreen
import ru.fourthproject.guessthenumber.ui.screens.mainScreen.MainScreen
import ru.fourthproject.guessthenumber.ui.screens.settingsScreen.SettingsScreen

@Composable
fun StartNavHost() {

    val navHostController =  rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screen.MainScreen.route,
        builder = {
            composable(Screen.MainScreen.route){
                MainScreen(navController = navHostController)
            }
            composable(Screen.AuthScreen.route){
                AuthScreen(navController = navHostController)
            }
            composable(Screen.SettingsScreen.route){
                SettingsScreen(navController = navHostController)
            }
        }
    )
}