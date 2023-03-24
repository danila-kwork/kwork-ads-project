package ru.lfyblf_cfif31.moneyproject_kwork.data.firebase.user.model

import android.util.Log

fun userSumMoney(
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int
): Double {
    val sum = countInterstitialAds * 0.01 +
            countInterstitialAdsClick * 0.025 +
            countRewardedAds * 0.025 +
            countRewardedAdsClick * 0.025

    Log.e("sum",sum.toString())

    return sum
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