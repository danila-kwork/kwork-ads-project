package ru.maproject.qrcode.navigation

sealed class Screen(val route: String) {
    object BarcodeCodeScanner: Screen("barcode_code_scanner")
}