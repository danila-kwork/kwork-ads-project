package en.cfif33.answer.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import en.cfif33.answer.data.firebase.question.QuestionDaraStore
import en.cfif33.answer.data.firebase.question.model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: QuestionDaraStore
):ViewModel() {

    fun getQuestionRandom(
        onSuccess:(Question) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        questionDaraStore.getQuestionRandom(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}