package ru.sixthproject.videogames.data.firebase.videoGame

import ru.sixthproject.videogames.data.firebase.videoGame.model.VideoGameQuestion
import com.google.firebase.database.FirebaseDatabase
import ru.sixthproject.videogames.data.firebase.videoGame.model.mapVideoGameQuestion
import javax.inject.Inject
import kotlin.random.Random

class VideoGameDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getVideoGame(
        onSuccess:(VideoGameQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val filmId = Random.nextInt(0,119).toString()

        db.reference.child("games").child(filmId).get()
            .addOnSuccessListener { onSuccess(it.mapVideoGameQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}