package ru.fifthproject.district.data.firebase.question

import ru.fifthproject.district.data.firebase.question.model.Question
import com.google.firebase.database.FirebaseDatabase
import ru.fifthproject.district.data.firebase.question.model.mapQuestion
import javax.inject.Inject
import kotlin.random.Random

class QuestionDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getQuestion(
        onSuccess:(Question) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val filmId = Random.nextInt(0,1131).toString()

        db.reference.child("russian_cities").child(filmId).get()
            .addOnSuccessListener { onSuccess(it.mapQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}