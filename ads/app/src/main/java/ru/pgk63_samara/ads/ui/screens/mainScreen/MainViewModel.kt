package ru.pgk63_samara.ads.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.pgk63_samara.ads.data.firebase.user.UserDataStore
import ru.pgk63_samara.ads.data.firebase.user.model.User
import ru.pgk63_samara.ads.data.firebase.withdrawalRequest.WithdrawalRequestDataStore
import ru.pgk63_samara.ads.data.firebase.withdrawalRequest.model.WithdrawalRequest
import ru.pgk63_samara.ads.data.firebase.words.WordsDataStore
import ru.pgk63_samara.ads.data.firebase.words.model.Word

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

    fun updateCountInterstitialAds(count: Int){
        userDataStore.updateCountInterstitialAds(count)
    }

    fun updateCountInterstitialAdsClick(count: Int){
        userDataStore.updateCountInterstitialAdsClick(count)
    }

    fun updateCountRewardedAds(count: Int){
        userDataStore.updateCountRewardedAds(count)
    }

    fun updateCountRewardedAdsClick(count: Int){
        userDataStore.updateCountRewardedAdsClick(count)
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