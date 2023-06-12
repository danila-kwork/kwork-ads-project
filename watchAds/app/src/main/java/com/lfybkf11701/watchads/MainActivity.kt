package com.lfybkf11701.watchads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lfybkf11701.watchads.ui.screens.MainScreen
import com.lfybkf11701.watchads.ui.screens.authScreen.AuthScreen
import com.lfybkf11701.watchads.ui.theme.WatchAdsTheme
import ru.andrey_one_project.moneyproject_kwork.ui.screens.settingsScreen.SettingsScreen
import com.lfybkf11701.watchads.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchAdsTheme {

                val navHostController = rememberNavController()

                val startDestination = if(Firebase.auth.currentUser == null)
                    "Auth"
                else
                    "Main"

                NavHost(
                    navController = navHostController,
                    startDestination = startDestination,
                    builder = {
                        composable("Main"){
                            MainScreen(
                                navController = navHostController
                            )
                        }

                        composable("Auth"){
                            AuthScreen(
                                navController = navHostController
                            )
                        }

                        composable("WithdrawalRequests"){
                            WithdrawalRequestsScreen(
                                navController = navHostController
                            )
                        }

                        composable("Settings"){
                            SettingsScreen(
                                navController = navHostController
                            )
                        }
                    }
                )
//                val count = remember { mutableStateOf(0) }
//
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = count.value.toString(),
//                        color = Color.Red,
//                        fontWeight = FontWeight.W900,
//                        fontSize = 32.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(10.dp)
//                    )
//
//                    Image(
//                        painterResource(id = R.drawable.right_click),
//                        contentDescription = null,
//                        modifier = Modifier.size(120.dp).clickable {
//                            count.value++
//                        }
//                    )
//
//                    YandexAdsBanner(
//                        modifier = Modifier.padding(5.dp)
//                    )
//                }
            }
        }
    }
}