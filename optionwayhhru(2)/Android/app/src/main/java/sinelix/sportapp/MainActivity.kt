package sinelix.sportapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import sinelix.sportapp.data.databse.UtilsDataStore
import sinelix.sportapp.data.remoteConfig.RemoteConfigDataStore
import sinelix.sportapp.ui.screens.LoadingScreen
import sinelix.sportapp.ui.screens.MainScreen
import sinelix.sportapp.ui.screens.NoInternetConnectionScreen
import sinelix.sportapp.ui.screens.WebViewScreen
import sinelix.sportapp.ui.theme.OptionwayhhruTheme
import sinelix.sportapp.utils.ConnectionInternet
import sinelix.sportapp.utils.checkIsEmu
import sinelix.sportapp.utils.isNetworkConnected

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OptionwayhhruTheme {
                val navController = rememberNavController()
                val connectionInternet = remember { ConnectionInternet(application) }
                var checkInternet by rememberSaveable { mutableStateOf(isNetworkConnected(this)) }
                var loading by rememberSaveable { mutableStateOf(true) }
                var localUrl by rememberSaveable { mutableStateOf<String?>(null) }
                var firebaseUrl by rememberSaveable { mutableStateOf<String?>(null) }
                val utilsDataStore = remember { UtilsDataStore(this@MainActivity) }
                val remoteConfigDataStore = remember(::RemoteConfigDataStore)

                val startDestination = if(loading && checkInternet)
                    "loading_screen"
                else if (!checkInternet || (firebaseUrl == null && localUrl == null))
                    "no_internet_screen"
                else if(localUrl != null)
                    "web_screen"
                else if(firebaseUrl == "" || checkIsEmu())
                    "main_screen"
                else {
                    utilsDataStore.saveUrl(firebaseUrl)
                    "web_screen"
                }

                connectionInternet.observe(this){
                    checkInternet = it
                }

                LaunchedEffect(key1 = Unit, block = {
                    localUrl = utilsDataStore.getUrl()
                    if(localUrl == null){
                        firebaseUrl = remoteConfigDataStore.getUrl()
                    }
                    loading = false
                })

//                LaunchedEffect(key1 = checkInternet, block = {
//                    if(checkInternet && localUrl == null && firebaseUrl == null){
//                        firebaseUrl = remoteConfigDataStore.getUrl()
//                    }
//                })

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
                    builder = {
                        composable("loading_screen"){
                            LoadingScreen()
                        }
                        composable("no_internet_screen"){
                            NoInternetConnectionScreen()
                        }
                        composable("main_screen"){
                            MainScreen()
                        }
                        composable("web_screen") {
                            WebViewScreen(url = localUrl ?: firebaseUrl ?: "")
                        }
                    }
                )
            }
        }
    }
}