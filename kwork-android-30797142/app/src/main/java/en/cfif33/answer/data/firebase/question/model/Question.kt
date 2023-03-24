package en.cfif33.answer.data.firebase.question.model

import com.google.firebase.database.DataSnapshot

data class Question(
    val date:String,
    val answer:String,
    val question:String
)

fun createQuestionLoading(): Question {
    return Question(
        date = "Загрузка",
        question = "Загрузка",
        answer = "Загрузка"
    )
}

fun DataSnapshot.mapQuestion(): Question {
    var date = this.child("date").value.toString()
    val answer = this.child("answer").value.toString()
    date = date
        .replace("January","Январь")
        .replace("February","Февраль")
        .replace("March","Март")
        .replace("April","Апрель")
        .replace("May","Май")
        .replace("June","Июнь")
        .replace("July","Июль")
        .replace("August","Август")
        .replace("September","Сентябрь")
        .replace("October","Октябрь")
        .replace("November","Ноябрь")
        .replace("December","Декабрь")

    return Question(
        date = date,
        answer = answer,
        question = if(date.isNotEmpty()) "Какое государство основано $date ?" else "date null"
    )
}