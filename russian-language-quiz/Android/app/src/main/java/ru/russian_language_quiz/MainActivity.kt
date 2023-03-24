package ru.russian_language_quiz

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
import ru.russian_language_quiz.ui.navigation.Screen
import ru.russian_language_quiz.ui.screens.achievementScreen.AchievementScreen
import ru.russian_language_quiz.ui.screens.achievementAdminScreen.AchievementAdminScreen
import ru.russian_language_quiz.ui.screens.addAchievementScreen.AddAchievementScreen
import ru.russian_language_quiz.ui.screens.adminScreen.AdminScreen
import ru.russian_language_quiz.ui.screens.adsScreen.AdsScreen
import ru.russian_language_quiz.ui.screens.authScreen.AuthScreen
import ru.russian_language_quiz.ui.screens.fonetikaScreen.FonetikaScreen
import ru.russian_language_quiz.ui.screens.kakPishetsyaScreen.KakPishetsyaScreen
import ru.russian_language_quiz.ui.screens.mainScreen.MainScreen
import ru.russian_language_quiz.ui.screens.questionsScreen.QuestionsScreen
import ru.russian_language_quiz.ui.screens.referralLinkScreen.ReferralLinkScreen
import ru.russian_language_quiz.ui.screens.rulesScreen.RulesScreen
import ru.russian_language_quiz.ui.screens.settingsScreen.SettingsScreen
import ru.russian_language_quiz.ui.screens.udarenieScreen.UdarenieScreen
import ru.russian_language_quiz.ui.screens.withdrawalRequestsScreen.WithdrawalRequestsScreen
import ru.russian_language_quiz.ui.theme.HistoryTheme

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
                    composable(Screen.KakPishetsya.route){
                        KakPishetsyaScreen(navController = navController)
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
                    composable(Screen.Fonetika.route){
                        FonetikaScreen(navController = navController)
                    }
                    composable(Screen.Rules.route){
                        RulesScreen(navController = navController)
                    }
                    composable(Screen.Udarenie.route){
                        UdarenieScreen(navController = navController)
                    }
                }
            }
        }
    }
}