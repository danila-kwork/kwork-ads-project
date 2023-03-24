package ru.lfyblf_cfif31.moneyproject_kwork

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.lfyblf_cfif31.moneyproject_kwork.data.firebase.utils.UtilsDataStore
import ru.lfyblf_cfif31.moneyproject_kwork.data.firebase.utils.model.Utils
import ru.lfyblf_cfif31.moneyproject_kwork.navigation.Screen
import ru.lfyblf_cfif31.moneyproject_kwork.ui.screens.authScreen.AuthScreen
import ru.lfyblf_cfif31.moneyproject_kwork.ui.screens.mainScreen.MainScreen
import ru.lfyblf_cfif31.moneyproject_kwork.ui.screens.settingsScreen.SettingsScreen
import ru.lfyblf_cfif31.moneyproject_kwork.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.lfyblf_cfif31.moneyproject_kwork.ui.theme.primaryBackground
import ru.lfyblf_cfif31.moneyproject_kwork.ui.theme.tintColor

val LocalNavController = staticCompositionLocalOf<NavController> { error("No navController provided") }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navHostController = rememberNavController()
            val utils = remember { mutableStateOf<Utils?>(null) }
            val utilsDataStore = remember(::UtilsDataStore)

            val startDestination = if(utils.value == null)
                "splash"
            else if(utils.value?.clicker == true)
                "clicker"
            else if(Firebase.auth.currentUser == null )
                Screen.Auth.route
            else
                Screen.Main.route

            LaunchedEffect(key1 = Unit, block = {
                utilsDataStore.get(onSuccess = { utils.value = it })
            })

            CompositionLocalProvider(
                LocalNavController provides navHostController
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = primaryBackground
                ) {
                    NavHost(
                        navController = navHostController,
                        startDestination = startDestination,
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

                            composable("splash"){
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(color = tintColor)
                                }
                            }

                            composable("clicker") {
                                var count by remember { mutableStateOf(0) }

                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = count.toString(),
                                        fontWeight = FontWeight.W900,
                                        modifier = Modifier.padding(5.dp),
                                        fontSize = 30.sp
                                    )

                                    IconButton(onClick = { count++ }) {
                                        Image(
                                            painterResource(id = R.drawable.cursor),
                                            null,
                                            modifier = Modifier.size(100.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}