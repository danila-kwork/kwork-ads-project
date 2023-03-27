package ru.maproject.qrcode.data.user.model

import com.google.firebase.database.DataSnapshot
import ru.maproject.qrcode.data.referral_link.model.ReferralLink
import ru.maproject.qrcode.data.utils.model.Utils

fun userSumMoneyVersion2(
    utils: Utils?,
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int,
    countBannerAds: Int = 0,
    countBannerAdsClick: Int = 0,
    achievementPrice: Double,
    referralLinkMoney: Double,
    giftMoney: Double
): Double {
    if(utils == null) return 0.0

    return countInterstitialAds * utils.interstitial_ads_price +
            countInterstitialAdsClick * utils.interstitial_ads_click_price +
            countRewardedAds * utils.rewarded_ads_price +
            countRewardedAdsClick * utils.rewarded_ads_click +
            countBannerAds * utils.banner_ads_price +
            countBannerAdsClick * utils.banner_ads_click_price +
            achievementPrice + referralLinkMoney + giftMoney
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

data class AchievementId(
    val id: String
)

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
    val userRole: UserRole = UserRole.BASE_USER,
    val countInterestingFact: Int = 0,
    val countQuestion: Int = 0,
    val achievementPrice: Double = 0.0,
    val referralLink: String? = null,
    val achievementIds: List<AchievementId> = emptyList(),
    val activeReferralLink: ReferralLink? = null,
    val referralLinkMoney: Double = 0.0,
    val giftMoney: Double = 0.0
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
        map["countInterestingFact"] = countInterestingFact
        map["countQuestion"] = countQuestion
        map["achievementIds"] = achievementIds
        map["achievementPrice"] = achievementPrice
        map["referralLinkMoney"] = referralLinkMoney
        map["giftMoney"] = giftMoney

        return map
    }
}

fun DataSnapshot.mapUser(): User {

    val activeReferralLinkChild = child("activeReferralLink")

    return User(
        id = child("id").value.toString(),
        email = child("email").value.toString(),
        password = child("password").value.toString(),
        countInterstitialAds = child("countInterstitialAds").value.toString().toInt(),
        countInterstitialAdsClick = child("countInterstitialAdsClick").value.toString().toInt(),
        countRewardedAds = child("countRewardedAds").value.toString().toInt(),
        countRewardedAdsClick = child("countRewardedAdsClick").value.toString().toInt(),
        countBannerAds = child("countBannerAds").value.toString().toInt(),
        countBannerAdsClick = child("countBannerAdsClick").value.toString().toInt(),
        userRole = enumValueOf(child("userRole").value.toString()),
        countInterestingFact = child("countInterestingFact").value.toString().toInt(),
        countQuestion = child("countQuestion").value.toString().toInt(),
        achievementIds = child("achievementId").children.map {
            AchievementId(it.child("id").value.toString())
        },
        achievementPrice = child("achievementPrice").value.toString().toDouble(),
        giftMoney = if(child("giftMoney").value.toString() == "null")
            0.0
        else
            child("giftMoney").value.toString().toDouble(),
        referralLink = child("referralLink").value.toString(),
        referralLinkMoney = if(child("referralLinkMoney").value.toString() == "null")
            0.0
        else
            child("referralLinkMoney").value.toString().toDouble(),
        activeReferralLink = if(activeReferralLinkChild.value == null)
            null
        else
            ReferralLink(
                id = activeReferralLinkChild.child("id").value.toString(),
                userId = activeReferralLinkChild.child("userId").value.toString()
            )
    )
}