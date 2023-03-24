package ru.russian_language_quiz.data.kakPishetsya

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.russian_language_quiz.data.kakPishetsya.model.KakPishetsya
import ru.russian_language_quiz.data.kakPishetsya.model.mapKakPishetsya

class KakPishetsyaDataStore {

    private val db = Firebase.database

    fun getAll(onSuccess: (List<KakPishetsya>) -> Unit) {
        db.reference.child("kak_pishetsya").get()
            .addOnSuccessListener {
                onSuccess(it.children.map { it.mapKakPishetsya() })
            }
    }
}