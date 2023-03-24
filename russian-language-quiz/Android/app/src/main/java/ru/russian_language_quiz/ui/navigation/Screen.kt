package ru.russian_language_quiz.ui.navigation

sealed class Screen(val route: String) {
    object Main: Screen("main_screen")
    object Auth: Screen("auth_screen")
    object Questions: Screen("questions_screen")
    object Admin: Screen("admin_screen")
    object Settings: Screen("settings_screen")
    object WithdrawalRequests: Screen("withdrawal_requests_screen")
    object Achievement: Screen("achievement_screen")
    object AddAchievement: Screen("add_achievement_screen")
    object Ads: Screen("ads_screen")
    object AchievementAdmin: Screen("achievement_admin_screen")
    object ReferralLink: Screen("referral_link_screen")
    object Fonetika: Screen("fonetika_screen")
    object KakPishetsya: Screen("kak_pishetsya_screen")
    object Rules: Screen("rules_screen")
    object Udarenie: Screen("udarenie_screen")
}