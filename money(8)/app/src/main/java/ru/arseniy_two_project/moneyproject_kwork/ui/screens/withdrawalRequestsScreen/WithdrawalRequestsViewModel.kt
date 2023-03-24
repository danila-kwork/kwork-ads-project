package ru.arseniy_two_project.moneyproject_kwork.ui.screens.withdrawalRequestsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.arseniy_two_project.moneyproject_kwork.data.firebase.withdrawalRequest.WithdrawalRequestDataStore
import ru.arseniy_two_project.moneyproject_kwork.data.firebase.withdrawalRequest.model.WithdrawalRequest

class WithdrawalRequestsViewModel(
    private val withdrawalRequestDataStore: WithdrawalRequestDataStore = WithdrawalRequestDataStore()
): ViewModel() {

    fun getWithdrawalRequests(onSuccess: (List<WithdrawalRequest>) -> Unit){
        withdrawalRequestDataStore.getAll(onSuccess) { message ->
            Log.e("getWithdrawalRequests",message)
        }
    }

    fun deleteWithdrawalRequest(id:String, onSuccess: () -> Unit, onError:(message: String) -> Unit){
        withdrawalRequestDataStore.deleteById(id, onSuccess, onError)
    }
}