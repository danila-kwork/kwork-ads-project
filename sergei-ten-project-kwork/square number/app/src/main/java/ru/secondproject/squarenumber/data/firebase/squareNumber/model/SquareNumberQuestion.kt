package ru.secondproject.squarenumber.data.firebase.squareNumber.model

data class SquareNumberQuestion(
    val question:String,
    val answer:String
)

fun createSquareNumberQuestionLoading(): SquareNumberQuestion {
    return SquareNumberQuestion(
        question = "Загрузка",
        answer = "Загрузка",
    )
}