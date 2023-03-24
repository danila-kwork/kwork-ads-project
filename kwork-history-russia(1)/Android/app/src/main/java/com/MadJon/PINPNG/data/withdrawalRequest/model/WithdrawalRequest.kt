package com.MadJon.PINPNG.data.withdrawalRequest.model

enum class WithdrawalRequestStatus(val text: String) {
    WAITING("Ожидает оплаты"),
    PAID(text = "Счет оплачен")
}

data class WithdrawalRequest(
    var id:String = "",
    var userId:String = "",
    val userEmail:String = "",
    val phoneNumber:String = "",
    val countInterstitialAds: Int = 0,
    val countInterstitialAdsClick: Int = 0,
    val countRewardedAds: Int = 0,
    val countRewardedAdsClick: Int = 0,
    val version: Int? = 1,
    val achievementPrice: Double = 0.0,
    val vpn: Boolean = false,
    val status: WithdrawalRequestStatus = WithdrawalRequestStatus.WAITING
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
        map["achievementPrice"] = achievementPrice
        map["vpn"] = vpn
        map["status"] = status
        version?.let {
            map["version"] = version
        }

        return map
    }
}