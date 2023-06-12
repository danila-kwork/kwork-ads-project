package com.lfybkf11701.watchads.data.firebase.withdrawalRequest.model

import com.lfybkf11701.watchads.common.Utils.isProbablyRunningOnEmulator

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
    val version: Int? = 1,
    val checkEmulator: Boolean? = null,
    val macAddress: String? = null,
    val checkVpn: Boolean? = null,
    val iPv4: String? = null,
    val iPv6: String? = null,
    val deviceName: String? = null,
    val androidVersion: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
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
        checkEmulator?.let {
            map["checkEmulator"] = checkEmulator
        }
        macAddress?.let {
            map["macAddress"] = macAddress
        }
        checkVpn?.let {
            map["checkVpn"] = checkVpn
        }
        iPv4?.let {
            map["iPv4"] = iPv4
        }
        iPv6?.let {
            map["iPv6"] = iPv6
        }
        deviceName?.let {
            map["deviceName"] = deviceName
        }
        androidVersion?.let {
            map["androidVersion"] = androidVersion
        }
        latitude?.let {
            map["latitude"] = latitude
        }
        longitude?.let {
            map["longitude"] = longitude
        }

        return map
    }
}