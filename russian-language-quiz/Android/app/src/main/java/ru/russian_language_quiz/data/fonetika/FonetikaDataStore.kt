package ru.russian_language_quiz.data.fonetika

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.russian_language_quiz.data.fonetika.model.Fonetika
import ru.russian_language_quiz.data.fonetika.model.mapFonetika

class FonetikaDataStore {

    private val db = Firebase.database

    fun getAll(onSuccess: (List<Fonetika>) -> Unit) {
        db.reference.child("fonetika").get()
            .addOnSuccessListener {
                onSuccess(it.children.map { it.mapFonetika() })
            }
    }
}