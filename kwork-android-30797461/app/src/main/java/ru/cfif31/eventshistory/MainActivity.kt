package ru.cfif31.eventshistory

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.cfif31.eventshistory.ui.navigation.Screen
import ru.cfif31.eventshistory.ui.screens.mainScreen.MainScreen
import ru.cfif31.eventshistory.ui.theme.EventsHistoryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {

            val navController = rememberNavController()

            EventsHistoryTheme(
                darkTheme = false
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Screen.MainScreen.route,
                    builder = {
                        composable(Screen.MainScreen.route){
                            MainScreen(navController = navController)
                        }
                    }
                )
            }
        }
    }
}
