package ru.fifthproject.district.data.firebase.question.model

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
    val district = this.child("district").value.toString()

    return Question(
        answer = district,
        question = if(nameCity.isNotEmpty()) "В каком федеральном округе находиться $nameCity ?" else "date null"
    )
}