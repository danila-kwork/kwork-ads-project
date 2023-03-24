package ru.biology_quiz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import ru.biology_quiz.ui.navigation.Screen
import ru.biology_quiz.ui.screens.achievementScreen.AchievementScreen
import ru.biology_quiz.ui.screens.achievementAdminScreen.AchievementAdminScreen
import ru.biology_quiz.ui.screens.addAchievementScreen.AddAchievementScreen
import ru.biology_quiz.ui.screens.adminScreen.AdminScreen
import ru.biology_quiz.ui.screens.adsScreen.AdsScreen
import ru.biology_quiz.ui.screens.articlesScreen.ArticlesScreen
import ru.biology_quiz.ui.screens.authScreen.AuthScreen
import ru.biology_quiz.ui.screens.interestingFactScreen.InterestingFactScreen
import ru.biology_quiz.ui.screens.mainScreen.MainScreen
import ru.biology_quiz.ui.screens.questionsScreen.QuestionsScreen
import ru.biology_quiz.ui.screens.referralLinkScreen.ReferralLinkScreen
import ru.biology_quiz.ui.screens.settingsScreen.SettingsScreen
import ru.biology_quiz.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.biology_quiz.ui.theme.HistoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HistoryTheme {
                val navController = rememberNavController()
                val auth = remember { FirebaseAuth.getInstance() }

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
                    composable(Screen.Articles.route){
                        ArticlesScreen(navController = navController)
                    }
                }
            }
        }
    }
}