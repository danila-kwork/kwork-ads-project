package ru.russian_language_quiz.data.fonetika.model

import com.google.firebase.database.DataSnapshot

data class Fonetika(
    val title: String,
    val url: String,
    val content: String
)

fun DataSnapshot.mapFonetika(): Fonetika {
    return Fonetika(
        title = child("title").value.toString(),
        url = child("url").value.toString(),
        content = child("content").value.toString(),
    )
}