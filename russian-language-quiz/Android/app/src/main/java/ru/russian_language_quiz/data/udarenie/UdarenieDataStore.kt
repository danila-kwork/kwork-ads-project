package ru.russian_language_quiz.data.udarenie

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.russian_language_quiz.data.udarenie.model.Udarenie
import ru.russian_language_quiz.data.udarenie.model.mapUdarenie

class UdarenieDataStore {

    private val db = Firebase.database

    fun getAll(onSuccess: (List<Udarenie>) -> Unit) {
        db.reference.child("udarenie").get()
            .addOnSuccessListener {
                onSuccess(it.children.map { it.mapUdarenie() })
            }
    }
}