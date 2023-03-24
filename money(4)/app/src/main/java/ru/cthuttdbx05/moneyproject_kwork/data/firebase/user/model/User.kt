package ru.cthuttdbx05.moneyproject_kwork.data.firebase.user.model

import ru.cthuttdbx05.moneyproject_kwork.data.firebase.utils.model.Utils

fun userSumMoneyVersion2(
    utils: Utils,
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int
): Double {

    return countInterstitialAds * utils.interstitial_ads_price +
            countInterstitialAdsClick * utils.interstitial_ads_click_price +
            countRewardedAds * utils.rewarded_ads_price +
            countRewardedAdsClick * utils.rewarded_ads_click
}

enum class UserRole {
    BASE_USER,
    ADMIN
}

fun createUserLoading(): User {
    return User(
        email = "Loading",
        password = "Loading",
    )
}

data class User(
    val id:String = "",
    val email:String = "",
    val password:String = "",
    val countInterstitialAds: Int = 0,
    val countInterstitialAdsClick: Int = 0,
    val countRewardedAds: Int = 0,
    val countRewardedAdsClick: Int = 0,
    val userRole: UserRole = UserRole.BASE_USER
) {
    fun dataMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String,Any>()

        map["id"] = id
        map["email"] = email
        map["password"] = password
        map["countInterstitialAds"] = countInterstitialAds
        map["countInterstitialAdsClick"] = countInterstitialAdsClick
        map["countRewardedAds"] = countRewardedAds
        map["countRewardedAdsClick"] = countRewardedAdsClick
        map["userRole"] = userRole

        return map
    }
}