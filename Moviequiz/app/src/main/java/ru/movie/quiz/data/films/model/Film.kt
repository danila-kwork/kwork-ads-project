package ru.movie.quiz.data.films.model

data class FilmResponse(
    val pagesCount: Int,
    val films: List<Film>
)

data class Film(
    val filmId: Int,
    val nameRu: String,
    val posterUrl: String,
    val rating: String
)