package ru.movie.quiz.common

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.movie.quiz.data.films.FilmApi

object RetrofitImpl {

    private val api: Retrofit = Retrofit.Builder()
        .baseUrl("https://kinopoiskapiunofficial.tech")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val filmsApi = api.create<FilmApi>()
}