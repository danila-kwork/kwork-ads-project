package ru.maproject.qrcode.navigation

sealed class Screen(val route: String) {
    object BarcodeCodeScanner: Screen("barcode_code_scanner")
    object Settings: Screen("settings_screen")
    object WithdrawalRequests: Screen("withdrawal_requests_creen")
    object Main: Screen("main_screen")
    object Admin: Screen("Admin")
    object ReferralLink: Screen("ReferralLink")
    object Auth: Screen("Auth")
    object Ads: Screen("Ads")
}