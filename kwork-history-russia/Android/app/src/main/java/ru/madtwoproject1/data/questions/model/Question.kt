package ru.madtwoproject1.data.questions.model

import com.google.firebase.database.DataSnapshot

data class Question(
    val image: String,
    val title: String,
    val content: List<QuestionItem>
)

data class QuestionItem(
    val answer:String,
    val question:String,
    val answers: List<Answer>
)

data class Answer(
    val answer: String
)

fun DataSnapshot.mapQuestion(): Question {
    return Question(
        image = child("image").value.toString(),
        title = child("title").value.toString(),
        content = child("content").children.map {
            QuestionItem(
                answer = it.child("answer").value.toString(),
                question = it.child("question").value.toString(),
                answers = it.child("answers").children.map {
                    Answer(
                        answer = it.child("answer").value.toString()
                    )
                }
            )
        }
    )
}