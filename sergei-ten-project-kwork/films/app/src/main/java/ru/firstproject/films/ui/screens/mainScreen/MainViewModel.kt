package ru.firstproject.films.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.firstproject.films.data.firebase.film.FilmDaraStore
import ru.firstproject.films.data.firebase.film.model.FilmQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: FilmDaraStore
):ViewModel() {

    fun getFilmRandom(
        onSuccess:(FilmQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        questionDaraStore.getFilmRandom(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}