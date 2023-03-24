package ru.sixthproject.videogames.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.sixthproject.videogames.data.firebase.videoGame.VideoGameDaraStore
import ru.sixthproject.videogames.data.firebase.videoGame.model.VideoGameQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: VideoGameDaraStore
):ViewModel() {

    fun getVideoGame(
        onSuccess:(VideoGameQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        questionDaraStore.getVideoGame(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}