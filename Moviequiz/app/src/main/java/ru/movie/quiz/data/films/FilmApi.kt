package ru.movie.quiz.data.films

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import ru.movie.quiz.data.films.model.FilmResponse
import ru.movie.quiz.data.interestingFacts.model.InterestingFactResponse

private val tokenList = listOf(
    "ab67ce7d-90cf-4d1b-b354-7474b82c9f38",
//    "56e1ce61-f95b-4a74-837a-4587769cc1b6"
)

interface FilmApi {

    @GET("/api/v2.2/films/top")
    suspend fun getFilmTop(
        @Query("page") page: Int,
        @Query("type") type: String = "TOP_250_BEST_FILMS",
        @Header("X-API-KEY") token: String = "ab67ce7d-90cf-4d1b-b354-7474b82c9f38"
    ): FilmResponse

    @GET("/api/v2.2/films/{id}/facts")
    suspend fun getFilmFacts(
        @Path("id") id: Int,
        @Header("X-API-KEY") token: String = "ab67ce7d-90cf-4d1b-b354-7474b82c9f38"
    ): InterestingFactResponse
}