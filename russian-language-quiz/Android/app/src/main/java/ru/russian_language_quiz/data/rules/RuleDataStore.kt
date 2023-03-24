package ru.russian_language_quiz.data.rules

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.russian_language_quiz.data.fonetika.model.Fonetika
import ru.russian_language_quiz.data.fonetika.model.mapFonetika
import ru.russian_language_quiz.data.rules.model.Rule
import ru.russian_language_quiz.data.rules.model.mapRule

class RuleDataStore {

    private val db = Firebase.database

    fun getAll(onSuccess: (List<Rule>) -> Unit) {
        db.reference.child("rules").get()
            .addOnSuccessListener {
                onSuccess(it.children.map { it.mapRule() })
            }
    }
}