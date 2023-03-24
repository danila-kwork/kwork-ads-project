package com.MadJon.PINPNG.data.achievement.model

import com.google.firebase.database.DataSnapshot

enum class AchievementType(val text: String) {
    QUESTIONS("Вопросы"),
    INTERESTING_FACT("Интересные факты")
}

data class Achievement(
    var id: String = "",
    val type: AchievementType,
    val count: Int,
    val reward: Double
)

fun DataSnapshot.mapAchievement(): Achievement {
    return Achievement(
        type = enumValueOf(child("type").value.toString()),
        count = child("count").value.toString().toInt(),
        id = child("id").value.toString(),
        reward = child("reward").value.toString().toDouble()
    )
}