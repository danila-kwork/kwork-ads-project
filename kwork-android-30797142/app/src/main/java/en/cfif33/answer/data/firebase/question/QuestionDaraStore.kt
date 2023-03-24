package en.cfif33.answer.data.firebase.question

import en.cfif33.answer.data.firebase.question.model.Question
import en.cfif33.answer.data.firebase.question.model.mapQuestion
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject
import kotlin.random.Random

class QuestionDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getQuestionRandom(
        onSuccess:(Question) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val eventId = Random.nextInt(0,189).toString()
        db.reference.child("question").child(eventId).get()
            .addOnSuccessListener { onSuccess(it.mapQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}