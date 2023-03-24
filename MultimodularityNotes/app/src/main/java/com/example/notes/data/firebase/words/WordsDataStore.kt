package com.example.notes.data.firebase.words

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.example.notes.data.firebase.words.model.Word
import com.example.notes.data.firebase.words.model.mapWord
import kotlin.random.Random

class WordsDataStore {

    private val database = Firebase.database

    fun getRandom(
        onSuccess:(Word) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val wordId = Random.nextInt(0,49167).toString()

        database.reference.child("words").child(wordId).get()
            .addOnSuccessListener { onSuccess(it.mapWord()) }
            .addOnFailureListener { onFailure(it.message ?: "ошибка") }
    }
}