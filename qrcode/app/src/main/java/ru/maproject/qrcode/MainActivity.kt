package ru.maproject.qrcode

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.maproject.qrcode.navigation.Screen
import ru.maproject.qrcode.ui.screens.BarcodeCodeScanner
import ru.maproject.qrcode.ui.screens.adminScreen.AdminScreen
import ru.maproject.qrcode.ui.screens.adsScreen.AdsScreen
import ru.maproject.qrcode.ui.screens.authScreen.AuthScreen
import ru.maproject.qrcode.ui.screens.mainScreen.MainScreen
import ru.maproject.qrcode.ui.screens.referralLinkScreen.ReferralLinkScreen
import ru.maproject.qrcode.ui.screens.settingsScreen.SettingsScreen
import ru.maproject.qrcode.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.maproject.qrcode.ui.theme.QrcodeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrcodeTheme {
                val navController = rememberNavController()
                val auth = remember(Firebase::auth)

                NavHost(
                    navController = navController,
                    startDestination = if(auth.currentUser == null)
                        Screen.Auth.route
                    else
                        Screen.BarcodeCodeScanner.route
                ){
                    composable(Screen.BarcodeCodeScanner.route){
                        BarcodeCodeScanner(navController = navController)
                    }
                    composable(Screen.Settings.route){
                        SettingsScreen(navController = navController)
                    }
                    composable(
                        "${Screen.WithdrawalRequests.route}/{status}",
                        arguments = listOf(
                            navArgument("status"){
                                type = NavType.StringType
                            }
                        )
                    ) {
                        WithdrawalRequestsScreen(
                            navController = navController,
                            withdrawalRequestStatus = enumValueOf(
                                it.arguments!!.getString("status").toString()
                            )
                        )
                    }
                    composable(Screen.Admin.route){
                        AdminScreen(navController = navController)
                    }
                    composable(Screen.ReferralLink.route){
                        ReferralLinkScreen(navController = navController)
                    }
                    composable(Screen.Auth.route){
                        AuthScreen(navController = navController)
                    }
                    composable(Screen.Ads.route){
                        AdsScreen()
                    }
                    composable(Screen.Main.route){
                        MainScreen(navController = navController)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    QrcodeTheme {
        Greeting("Android")
    }
}