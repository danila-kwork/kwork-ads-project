package ru.biology_quiz.data.articles.model

import com.google.firebase.database.DataSnapshot

data class Article(
    val title: String,
    val url: String,
    val teacher: String,
    val author: String,
    val html: String
)

fun DataSnapshot.mapArticle(): Article {
    return Article(
        title = child("title").value.toString(),
        url = child("url").value.toString(),
        teacher = child("teacher").value.toString(),
        html = child("html").value.toString(),
        author = child("author").value.toString(),
    )
}