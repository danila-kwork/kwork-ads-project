package com.example.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.notes.navigation.Screen
import com.example.notes.ui.screens.authScreen.AuthScreen
import com.example.notes.ui.screens.mainScreen.MainScreen
import com.example.notes.ui.screens.settingsScreen.SettingsScreen
import com.example.notes.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import com.example.notes.ui.theme.primaryBackground

val LocalNavController = staticCompositionLocalOf<NavController> { error("No navController provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navHostController = rememberNavController()

            CompositionLocalProvider(
                LocalNavController provides navHostController
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = primaryBackground
                ) {
                    NavHost(
                        navController = navHostController,
                        startDestination = if(Firebase.auth.currentUser == null ) Screen.Auth.route else Screen.Main.route,
                        builder = {
                            composable(Screen.Main.route){
                                MainScreen()
                            }
                            composable(Screen.Auth.route){
                                AuthScreen()
                            }
                            composable(Screen.WithdrawalRequests.route){
                                WithdrawalRequestsScreen()
                            }
                            composable(Screen.Settings.route){
                                SettingsScreen()
                            }
                        }
                    )
                }
            }
        }
    }
}