package ru.firstproject.films.data.firebase.film.model

import com.google.firebase.database.DataSnapshot

data class FilmQuestion(
    val question:String,
    val year:String
)

fun createFilmQuestionLoading(): FilmQuestion {
    return FilmQuestion(
        question = "Загрузка",
        year = "Загрузка",
    )
}

fun DataSnapshot.mapFilmQuestion(): FilmQuestion {
    val russianName = this.child("russian_name").value.toString()
    val year = this.child("year").value.toString()

    return FilmQuestion(
        year = year,
        question = if(russianName.isNotEmpty()) "В каком году вышел фильм '$russianName' ?" else "date null"
    )
}