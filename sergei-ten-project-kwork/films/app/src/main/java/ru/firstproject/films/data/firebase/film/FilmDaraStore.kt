package ru.firstproject.films.data.firebase.film

import ru.firstproject.films.data.firebase.film.model.FilmQuestion
import com.google.firebase.database.FirebaseDatabase
import ru.firstproject.films.data.firebase.film.model.mapFilmQuestion
import javax.inject.Inject
import kotlin.random.Random

class FilmDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getFilmRandom(
        onSuccess:(FilmQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        val filmId = Random.nextInt(0,96).toString()

        db.reference.child("films").child(filmId).get()
            .addOnSuccessListener { onSuccess(it.mapFilmQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }

    }
}