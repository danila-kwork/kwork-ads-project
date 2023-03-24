package ru.russian_language_quiz.data.udarenie.model

import com.google.firebase.database.DataSnapshot

data class Udarenie(
    val title: String,
    val url: String,
    val content: String
)

fun DataSnapshot.mapUdarenie(): Udarenie {
    return Udarenie(
        title = child("title").value.toString(),
        url = child("url").value.toString(),
        content = child("content").value.toString(),
    )
}