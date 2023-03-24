package ru.russian_language_quiz.data.kakPishetsya.model

import com.google.firebase.database.DataSnapshot

data class KakPishetsya(
    val title: String,
    val url: String,
    val content: String
)

fun DataSnapshot.mapKakPishetsya(): KakPishetsya {
    return KakPishetsya(
        title = child("title").value.toString(),
        url = child("url").value.toString(),
        content = child("content").value.toString(),
    )
}