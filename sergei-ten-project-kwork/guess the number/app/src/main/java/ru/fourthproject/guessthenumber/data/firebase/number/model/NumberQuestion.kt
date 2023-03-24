package ru.fourthproject.guessthenumber.data.firebase.number.model

data class NumberQuestion(
    var question:String,
    val answer:String
)

fun createNumberQuestionLoading(): NumberQuestion {
    return NumberQuestion(
        question = "Загрузка",
        answer = "Загрузка",
    )
}