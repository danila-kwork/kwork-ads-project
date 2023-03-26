package com.example.notes.data.firebase.user.model

import com.example.notes.data.firebase.utils.model.Utils

fun userSumMoney(
    utils: Utils?,
    countAds: Int,
    countAnswers: Int,
    countAdsClick: Int,
    countClickWatchAds: Int
): Double {
    utils ?: return 0.0

    return countAds * utils.adsPrice +
            countAnswers * utils.answersPrice +
            countAdsClick * utils.adsClickPrice +
            countClickWatchAds * utils.clickWatchAdsPrice
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
    val countAds:Int = 0,
    val countAdsClick: Int = 0,
    val countAnswers:Int = 0,
    val countClickWatchAds:Int = 0,
    val userRole: UserRole = UserRole.BASE_USER
) {
    fun dataMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String,Any>()

        map["id"] = id
        map["email"] = email
        map["password"] = password
        map["countAds"] = countAds
        map["countAdsClick"] = countAdsClick
        map["countAnswers"] = countAnswers
        map["countClickWatchAds"] = countClickWatchAds
        map["userRole"] = userRole

        return map
    }
}