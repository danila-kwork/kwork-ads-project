package cfif31.kwork.namevalues.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import cfif31.kwork.namevalues.data.firebase.name.NameDaraStore
import cfif31.kwork.namevalues.data.firebase.name.model.Name
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: NameDaraStore
):ViewModel() {

    fun getNameRandom(
        onSuccess:(Name) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        questionDaraStore.getNameRandom(
            onSuccess = onSuccess,
            onFailure = onFailure
        )
    }
}