package ru.ninthproject.countrycode.data.firebase.question

import ru.ninthproject.countrycode.data.firebase.question.model.Question
import com.google.firebase.database.FirebaseDatabase
import ru.ninthproject.countrycode.data.firebase.question.model.mapQuestion
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

        db.reference.child("countries").child(filmId).get()
            .addOnSuccessListener { onSuccess(it.mapQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}