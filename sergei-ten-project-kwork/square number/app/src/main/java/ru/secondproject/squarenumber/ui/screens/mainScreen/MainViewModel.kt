package ru.secondproject.squarenumber.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.secondproject.squarenumber.data.firebase.squareNumber.SquareNumberDaraStore
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.secondproject.squarenumber.data.firebase.squareNumber.model.SquareNumberQuestion
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questionDaraStore: SquareNumberDaraStore
):ViewModel() {

    fun getSquareNumberRandom(): SquareNumberQuestion = questionDaraStore.getSquareNumberRandom()

}