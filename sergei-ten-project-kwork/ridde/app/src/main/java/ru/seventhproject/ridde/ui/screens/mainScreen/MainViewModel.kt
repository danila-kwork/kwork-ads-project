package ru.seventhproject.ridde.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.seventhproject.ridde.data.firebase.riddde.RidddeGameDaraStore
import ru.seventhproject.ridde.data.firebase.riddde.model.RidddeGameQuestion
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: RidddeGameDaraStore
):ViewModel() {

    fun getRiddle(
        onSuccess:(RidddeGameQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        questionDaraStore.getRiddle(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}