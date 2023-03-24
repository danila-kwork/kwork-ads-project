package ru.fourthproject.guessthenumber.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.fourthproject.guessthenumber.data.firebase.number.NumberDaraStore
import ru.fourthproject.guessthenumber.data.firebase.number.model.NumberQuestion
import ru.fourthproject.guessthenumber.data.firebase.user.UserDataStore
import ru.fourthproject.guessthenumber.data.firebase.user.model.User

class MainViewModel:ViewModel() {

    private val questionDaraStore = NumberDaraStore()
    private val userDataStore = UserDataStore()

    fun getNumberRandom(): NumberQuestion = questionDaraStore.getNumberRandom()

    fun getUser(onSuccess:(User) -> Unit) {
        userDataStore.get(onSuccess)
    }
}