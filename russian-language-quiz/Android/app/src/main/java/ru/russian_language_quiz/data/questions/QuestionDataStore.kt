package ru.russian_language_quiz.data.questions

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.russian_language_quiz.data.questions.model.Question
import ru.russian_language_quiz.data.questions.model.mapQuestion

class QuestionDataStore {

    private val db = Firebase.database

    fun getQuestionList(
        onSuccess: (List<Question>) -> Int
    ) {
        val questionsList = ArrayList<Question>()

        db.reference.child("questions").get()
            .addOnSuccessListener {
                it.children.forEach {
                    questionsList.add(it.mapQuestion())
                }
                onSuccess(questionsList)
            }
 }
}