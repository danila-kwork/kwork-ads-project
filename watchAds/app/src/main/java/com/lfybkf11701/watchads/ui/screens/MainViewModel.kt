package com.lfybkf11701.watchads.ui.screens

import androidx.lifecycle.ViewModel
import com.lfybkf11701.watchads.data.firebase.user.UserDataStore
import com.lfybkf11701.watchads.data.firebase.user.model.User
import com.lfybkf11701.watchads.data.firebase.withdrawalRequest.WithdrawalRequestDataStore
import com.lfybkf11701.watchads.data.firebase.withdrawalRequest.model.WithdrawalRequest

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

    fun updateCountBannerAdsClick(count: Int){
        userDataStore.updateCountBannerAdsClick(count)
    }

    fun updateCountBannerAds(count: Int){
        userDataStore.updateCountBannerAds(count)
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