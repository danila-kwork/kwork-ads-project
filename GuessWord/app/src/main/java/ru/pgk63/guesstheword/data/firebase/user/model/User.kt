package ru.pgk63.guesstheword.data.firebase.user.model

fun userSumAds(countAds:Int,countAnswers:Int): Double {
    return countAds * 0.01 + countAnswers * 0.0001
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
    val countAnswers:Int = 0,
    val userRole: UserRole = UserRole.BASE_USER
) {
    fun dataMap(): MutableMap<String, Any> {
        val map = mutableMapOf<String,Any>()

        map["id"] = id
        map["email"] = email
        map["password"] = password
        map["countAds"] = countAds
        map["countAnswers"] = countAnswers
        map["userRole"] = userRole

        return map
    }
}