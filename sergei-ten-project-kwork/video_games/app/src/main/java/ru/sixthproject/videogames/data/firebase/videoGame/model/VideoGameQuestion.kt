package ru.sixthproject.videogames.data.firebase.videoGame.model

import com.google.firebase.database.DataSnapshot
import java.text.SimpleDateFormat
import java.util.*

data class VideoGameQuestion(
    val question: String,
    val year: String,
)

fun createFilmQuestionLoading(): VideoGameQuestion {
    return VideoGameQuestion(
        question = "Загрузка",
        year = "Загрузка",
    )
}

fun DataSnapshot.mapVideoGameQuestion(): VideoGameQuestion {
    val name = this.child("name").value.toString()
    val released = this.child("released").value.toString()

    val formatIn = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val formatOut = SimpleDateFormat("yyyy", Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.time = formatIn.parse(released) as Date

    val newDate: String = formatOut.format(calendar.time)

    return VideoGameQuestion(
        year = newDate,
        question = if(name.isNotEmpty()) "В каком году вышела игра '$name' ?" else "date null"
    )
}