package com.example.notes.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import com.example.notes.data.firebase.user.UserDataStore
import com.example.notes.data.firebase.user.model.User
import com.example.notes.data.firebase.withdrawalRequest.WithdrawalRequestDataStore
import com.example.notes.data.firebase.withdrawalRequest.model.WithdrawalRequest
import com.example.notes.data.firebase.words.WordsDataStore
import com.example.notes.data.firebase.words.model.Word

class MainViewModel(
    private val userDataStore: UserDataStore = UserDataStore(),
    private val wordsDataStore: WordsDataStore = WordsDataStore(),
    private val withdrawalRequestDataStore: WithdrawalRequestDataStore = WithdrawalRequestDataStore()
): ViewModel() {
    fun getRandom(
        onSuccess:(Word) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ){
        wordsDataStore.getRandom(onSuccess, onFailure)
    }

    fun getUser(onSuccess:(User) -> Unit){
        userDataStore.get(onSuccess)
    }

    fun updateCountAds(count: Int){
        userDataStore.updateCountAds(count)
    }

    fun updateCountAdsClick(count: Int){
        userDataStore.updateCountAdsClick(count)
    }

    fun updateCountAnswers(count: Int){
        userDataStore.updateCountAnswers(count)
    }

    fun sendWithdrawalRequest(
        withdrawalRequest: WithdrawalRequest,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ) {
        withdrawalRequestDataStore.create(
            withdrawalRequest = withdrawalRequest,
            onCompleteListener = {
                if(it.isSuccessful){
                    onSuccess()
                }else{
                    onError(it.exception?.message ?: "Ошибка")
                }
            }
        )
    }
}