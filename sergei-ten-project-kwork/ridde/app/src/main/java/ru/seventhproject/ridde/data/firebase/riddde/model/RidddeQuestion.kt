package ru.seventhproject.ridde.data.firebase.riddde.model

import com.google.firebase.database.DataSnapshot
import java.util.*

data class RidddeGameQuestion(
    val question: String,
    val answer: String,
)

fun createRidddeQuestionLoading(): RidddeGameQuestion {
    return RidddeGameQuestion(
        question = "Загрузка",
        answer = "Загрузка",
    )
}

fun DataSnapshot.mapRidddeQuestion(): RidddeGameQuestion {

    val answer = this.child("answer").value.toString()
    val riddle = this.child("riddle").value.toString()

    return RidddeGameQuestion(
        answer = answer,
        question = riddle
    )
}