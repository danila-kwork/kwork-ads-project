package ru.russian_language_quiz.data.rules.model

import com.google.firebase.database.DataSnapshot

data class Rule(
    val title: String,
    val url: String,
    val content: String
)

fun DataSnapshot.mapRule(): Rule {
    return Rule(
        title = child("title").value.toString(),
        url = child("url").value.toString(),
        content = child("content").value.toString(),
    )
}