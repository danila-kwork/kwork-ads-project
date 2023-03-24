package ru.ninthproject.countrycode.data.firebase.question.model

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
    val capital = this.child("capital").value.toString()
    val code = this.child("code").value.toString()

    return Question(
        answer = code,
        question = if(capital.isNotEmpty()) "Какой код страны\n$capital ?" else "date null"
    )
}