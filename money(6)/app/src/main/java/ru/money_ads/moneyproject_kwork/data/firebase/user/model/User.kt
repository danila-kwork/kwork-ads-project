package ru.money_ads.moneyproject_kwork.data.firebase.user.model

import android.util.Log
import ru.money_ads.moneyproject_kwork.data.firebase.utils.model.Utils

@Deprecated("userSumMoneyVersion2")
fun userSumMoneyVersion1(
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int
): Double {

    return countInterstitialAds * 0.005 +
            countInterstitialAdsClick * 0.015 +
            countRewardedAds * 0.005 +
            countRewardedAdsClick * 0.04
}

fun userSumMoneyVersion2(
    utils: Utils,
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int,
    countBannerAdsClick: Int,
    countBannerAds: Int,
): Double {
    return countInterstitialAds * utils.interstitial_ads_price +
            countInterstitialAdsClick * utils.interstitial_ads_click_price +
            countRewardedAds * utils.rewarded_ads_price +
            countRewardedAdsClick * utils.rewarded_ads_click +
            countBannerAds * utils.banner_ads_price +
            countBannerAdsClick * utils.banner_ads_click_price
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
    val countBannerAdsClick: Int = 0,
    val countBannerAds: Int = 0,
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
        map["countBannerAds"] = countBannerAds
        map["countBannerAdsClick"] = countBannerAdsClick
        map["userRole"] = userRole

        return map
    }
}