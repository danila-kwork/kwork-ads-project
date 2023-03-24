package ru.tenthproject.guessmovie.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.tenthproject.guessmovie.ui.screens.mainScreen.MainScreen

@Composable
fun StartNavHost() {

    val navHostController =  rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = Screen.MainScreen.route,
        builder = {
            composable(Screen.MainScreen.route){
                MainScreen(
                    navController = navHostController
                )
            }
        }
    )
}