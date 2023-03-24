package ru.tenthproject.guessmovie.data.firebase.question.model

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
    val russianName = this.child("russian_name").value.toString()
    val actors = this.child("actors").value.toString()
    val year = this.child("year").value.toString()

    return Question(
        answer = russianName,
        question = "Угадай фильм\nВ главной роле $actors\nФильм вышел $year"
    )
}