package ru.thirdproject.population.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.thirdproject.population.data.firebase.population.PopulationDaraStore
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.thirdproject.population.data.firebase.population.model.PopulationQuestion
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: PopulationDaraStore
):ViewModel() {

    fun getSquareNumberRandom(
        onSuccess:(PopulationQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ) {
        questionDaraStore.getPopulationRandom(onSuccess, onFailure)
    }

}