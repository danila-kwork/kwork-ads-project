package ru.movie.quiz.ui.screens.interestingFactScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import ru.movie.quiz.data.films.FilmDataStore

class InterestingFactViewModel : ViewModel() {

    private val filmDataStore = FilmDataStore()

    val getFilmsTop = filmDataStore.getFilmsTop().cachedIn(viewModelScope)
}