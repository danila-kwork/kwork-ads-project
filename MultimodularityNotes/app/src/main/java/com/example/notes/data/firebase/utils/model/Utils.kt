package com.example.notes.data.firebase.utils.model

import com.google.firebase.database.DataSnapshot

data class Utils(
    val banner_yandex_ads_id: String,
    val interstitial_yandex_ads_id: String,
    val min_price_withdrawal_request: Double,
    val banner_ads_click_price: Double,
    val rewarded_yandex_ads_id: String,
    val adsPrice: Double,
    val adsClickPrice: Double,
    val answersPrice: Double,
    val clickWatchAdsPrice: Double,
)

fun DataSnapshot.mapUtils(): Utils {
    return Utils(
        banner_yandex_ads_id = child("banner_yandex_ads_id").value.toString(),
        interstitial_yandex_ads_id = child("interstitial_yandex_ads_id").value.toString(),
        min_price_withdrawal_request = child("min_price_withdrawal_request").value.toString().toDouble(),
        rewarded_yandex_ads_id = child("rewarded_yandex_ads_id").value.toString(),
        banner_ads_click_price = child("banner_ads_click_price").value.toString().toDouble(),
        adsPrice = child("adsPrice").value.toString().toDouble(),
        adsClickPrice = child("adsClickPrice").value.toString().toDouble(),
        answersPrice = child("answersPrice").value.toString().toDouble(),
        clickWatchAdsPrice = child("clickWatchAdsPrice").value.toString().toDouble(),
    )
}