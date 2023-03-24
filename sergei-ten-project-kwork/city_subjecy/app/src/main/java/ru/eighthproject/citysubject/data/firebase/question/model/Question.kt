package ru.eighthproject.citysubject.data.firebase.question.model

import com.google.firebase.database.DataSnapshot

data class Question(
    val question:String,
    val answer:String
)

fun createQuestionLoading(): Question {
    return Question(
        question = "Загрузка",
        answer = "Загрузка",
    )
}

fun DataSnapshot.mapQuestion(): Question {
    val nameCity = this.child("name").value.toString()
    val subject = this.child("subject").value.toString()

    return Question(
        answer = subject,
        question = if(nameCity.isNotEmpty()) "В каком регионе находиться $nameCity ?" else "date null"
    )
}