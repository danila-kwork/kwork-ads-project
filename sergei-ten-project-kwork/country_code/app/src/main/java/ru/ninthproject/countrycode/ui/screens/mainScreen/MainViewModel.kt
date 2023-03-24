package ru.ninthproject.countrycode.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.ninthproject.countrycode.data.firebase.question.QuestionDaraStore
import ru.ninthproject.countrycode.data.firebase.question.model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: QuestionDaraStore
):ViewModel() {

    fun getQuestion(
        onSuccess:(Question) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        questionDaraStore.getQuestion(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}