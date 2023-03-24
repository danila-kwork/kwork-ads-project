package ru.thirdproject.population.ui.screens.mainScreen

import androidx.lifecycle.ViewModel
import ru.thirdproject.population.data.firebase.population.PopulationDaraStore
import ru.thirdproject.population.data.firebase.population.model.PopulationQuestion
import ru.thirdproject.population.data.firebase.user.UserDataStore
import ru.thirdproject.population.data.firebase.user.model.User
import ru.thirdproject.population.data.firebase.withdrawalRequest.WithdrawalRequestDataStore
import ru.thirdproject.population.data.firebase.withdrawalRequest.model.WithdrawalRequest

class MainViewModel(
    private val userDataStore: UserDataStore = UserDataStore(),
    private val withdrawalRequestDataStore: WithdrawalRequestDataStore = WithdrawalRequestDataStore(),
    private val questionDaraStore: PopulationDaraStore = PopulationDaraStore()
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

    fun getPopulationRandom(
        onSuccess:(PopulationQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ) {
        questionDaraStore.getPopulationRandom(onSuccess, onFailure)
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