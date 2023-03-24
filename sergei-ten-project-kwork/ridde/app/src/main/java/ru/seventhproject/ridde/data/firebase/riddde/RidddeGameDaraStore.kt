package ru.seventhproject.ridde.data.firebase.riddde

import ru.seventhproject.ridde.data.firebase.riddde.model.RidddeGameQuestion
import com.google.firebase.database.FirebaseDatabase
import ru.seventhproject.ridde.data.firebase.riddde.model.mapRidddeQuestion
import javax.inject.Inject
import kotlin.random.Random

class RidddeGameDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getRiddle(
        onSuccess:(RidddeGameQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val filmId = Random.nextInt(0,35).toString()

        db.reference.child("ridde").child(filmId).get()
            .addOnSuccessListener { onSuccess(it.mapRidddeQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}