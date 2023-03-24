package ru.thirdproject.population.data.firebase.withdrawalRequest.model

data class WithdrawalRequest(
    var id:String = "",
    var userId:String = "",
    val userEmail:String = "",
    val phoneNumber:String = "",
    val countInterstitialAds: Int = 0,
    val countInterstitialAdsClick: Int = 0,
    val countRewardedAds: Int = 0,
    val countRewardedAdsClick: Int = 0,
    val countBannerAds: Int = 0,
    val countBannerAdsClick: Int = 0,
    val version: Int? = 1
){
    fun dataMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String,Any>()

        map["id"] = id
        map["userId"] = userId
        map["userEmail"] = userEmail
        map["phoneNumber"] = phoneNumber
        map["countInterstitialAds"] = countInterstitialAds
        map["countInterstitialAdsClick"] = countInterstitialAdsClick
        map["countRewardedAds"] = countRewardedAds
        map["countRewardedAdsClick"] = countRewardedAdsClick
        map["countBannerAdsClick"] = countBannerAdsClick
        map["countBannerAds"] = countBannerAds
        version?.let {
            map["version"] = version
        }

        return map
    }
}