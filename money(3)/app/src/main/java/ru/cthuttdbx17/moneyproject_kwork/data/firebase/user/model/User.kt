package ru.cthuttdbx17.moneyproject_kwork.data.firebase.user.model

@Deprecated("using version 2")
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
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int
): Double {

    return countInterstitialAds * 0.005 +
            countInterstitialAdsClick * 0.015 +
            countRewardedAds * 0.005 +
            countRewardedAdsClick * 0.05
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