package ru.movie.quiz.data.films

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.movie.quiz.common.RetrofitImpl
import ru.movie.quiz.data.films.model.Film

class FilmDataStore {

    private val filmApi = RetrofitImpl.filmsApi

    fun getFilmsTop(): Flow<PagingData<Film>> {
        return Pager(PagingConfig(pageSize = 13)){
            FilmsPagingSource(filmApi)
        }.flow
    }
}