package ru.madtwoproject3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.madtwoproject3.ui.navigation.Screen
import ru.madtwoproject3.ui.screens.achievementAdminScreen.AchievementAdminScreen
import ru.madtwoproject3.ui.screens.achievementScreen.AchievementScreen
import ru.madtwoproject3.ui.screens.addAchievementScreen.AddAchievementScreen
import ru.madtwoproject3.ui.screens.adminScreen.AdminScreen
import ru.madtwoproject3.ui.screens.adsScreen.AdsScreen
import ru.madtwoproject3.ui.screens.authScreen.AuthScreen
import ru.madtwoproject3.ui.screens.interestingFactScreen.InterestingFactScreen
import ru.madtwoproject3.ui.screens.mainScreen.MainScreen
import ru.madtwoproject3.ui.screens.questionsScreen.QuestionsScreen
import ru.madtwoproject3.ui.screens.referralLinkScreen.ReferralLinkScreen
import ru.madtwoproject3.ui.screens.settingsScreen.SettingsScreen
import ru.madtwoproject3.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.madtwoproject3.ui.theme.HistoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoryTheme {
                val navController = rememberNavController()
                val auth = remember(Firebase::auth)

                

                NavHost(
                    navController = navController,
                    startDestination = if(auth.currentUser == null)
                        Screen.Auth.route
                    else
                        Screen.Main.route
                ){
                    composable(Screen.Auth.route){
                        AuthScreen(navController = navController)
                    }
                    composable(Screen.Main.route){
                        MainScreen(navController = navController)
                    }
                    composable(Screen.Admin.route){
                        AdminScreen(navController = navController)
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
                    composable(Screen.Questions.route){
                        QuestionsScreen(navController = navController)
                    }
                    composable(Screen.InterestingFact.route){
                        InterestingFactScreen(navController = navController)
                    }
                    composable(Screen.Achievement.route){
                        AchievementScreen(navController = navController)
                    }
                    composable(Screen.AddAchievement.route){
                        AddAchievementScreen(navController = navController)
                    }
                    composable(Screen.Ads.route){
                        AdsScreen()
                    }
                    composable(Screen.AchievementAdmin.route){
                        AchievementAdminScreen()
                    }
                    composable(Screen.ReferralLink.route){
                        ReferralLinkScreen(navController = navController)
                    }
                }
            }
        }
    }
}