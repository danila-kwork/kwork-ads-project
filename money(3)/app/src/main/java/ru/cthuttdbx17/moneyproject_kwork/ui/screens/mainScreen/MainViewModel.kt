package ru.cthuttdbx17.moneyproject_kwork.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.user.UserDataStore
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.user.model.User
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.withdrawalRequest.WithdrawalRequestDataStore
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.withdrawalRequest.model.WithdrawalRequest

class MainViewModel(
    private val userDataStore: UserDataStore = UserDataStore(),
    private val withdrawalRequestDataStore: WithdrawalRequestDataStore = WithdrawalRequestDataStore()
): ViewModel() {

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