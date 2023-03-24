package ru.madtwoproject1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ru.madtwoproject1.ui.navigation.Screen
import ru.madtwoproject1.ui.screens.achievementScreen.AchievementScreen
import ru.madtwoproject1.ui.screens.achievementAdminScreen.AchievementAdminScreen
import ru.madtwoproject1.ui.screens.addAchievementScreen.AddAchievementScreen
import ru.madtwoproject1.ui.screens.adminScreen.AdminScreen
import ru.madtwoproject1.ui.screens.adsScreen.AdsScreen
import ru.madtwoproject1.ui.screens.authScreen.AuthScreen
import ru.madtwoproject1.ui.screens.interestingFactScreen.InterestingFactScreen
import ru.madtwoproject1.ui.screens.mainScreen.MainScreen
import ru.madtwoproject1.ui.screens.questionsScreen.QuestionsScreen
import ru.madtwoproject1.ui.screens.referralLinkScreen.ReferralLinkScreen
import ru.madtwoproject1.ui.screens.settingsScreen.SettingsScreen
import ru.madtwoproject1.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.madtwoproject1.ui.theme.HistoryTheme

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