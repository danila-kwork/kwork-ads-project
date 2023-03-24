package ru.pgk63_samara.ads

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
import ru.pgk63_samara.ads.navigation.Screen
import ru.pgk63_samara.ads.ui.screens.authScreen.AuthScreen
import ru.pgk63_samara.ads.ui.screens.mainScreen.MainScreen
import ru.pgk63_samara.ads.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.pgk63_samara.ads.ui.theme.primaryBackground

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
                        }
                    )
                }
            }
        }
    }
}