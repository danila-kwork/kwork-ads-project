package ru.movie.quiz.data.withdrawalRequest

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ru.movie.quiz.data.referral_link.model.ReferralLink
import ru.movie.quiz.data.user.model.userSumMoneyVersion2
import ru.movie.quiz.data.utils.model.Utils
import ru.movie.quiz.data.withdrawalRequest.model.WithdrawalRequest
import ru.movie.quiz.data.withdrawalRequest.model.WithdrawalRequestStatus
import java.util.UUID

class WithdrawalRequestDataStore {

    private val auth = Firebase.auth
    private val database = Firebase.database
    private val userId = auth.currentUser!!.uid

    fun getAll(onSuccess: (List<WithdrawalRequest>) -> Unit, onError:(message:String) -> Unit) {

        val withdrawalRequests = mutableListOf<WithdrawalRequest>()

        database.reference.child("withdrawal_request").get()
            .addOnSuccessListener {
                for (i in it.children){
                    i.getValue<WithdrawalRequest>()?.let {
                        withdrawalRequests.add(it)
                    }
                }

                onSuccess(withdrawalRequests)
            }
            .addOnFailureListener {
                onError(it.message ?: "Ошибка")
            }
    }

    fun create(
        utils: Utils,
        activeReferralLink: ReferralLink? = null,
        withdrawalRequest: WithdrawalRequest,
        onCompleteListener: (Task<Void>) -> Unit = {}
    ) {
        val id = UUID.randomUUID().toString()

        withdrawalRequest.id = id
        withdrawalRequest.userId = userId

        val sum = userSumMoneyVersion2(
            utils = utils,
            countInterstitialAds = withdrawalRequest.countInterstitialAds,
            countInterstitialAdsClick = withdrawalRequest.countInterstitialAdsClick,
            countRewardedAds = withdrawalRequest.countRewardedAds,
            countRewardedAdsClick = withdrawalRequest.countRewardedAdsClick,
            achievementPrice = withdrawalRequest.achievementPrice,
            referralLinkMoney = withdrawalRequest.referralLinkMoney,
            giftMoney = withdrawalRequest.giftMoney
        )

        database.reference.child("withdrawal_request").child(id)
            .updateChildren(withdrawalRequest.dataMap())
            .addOnCompleteListener { it ->
                if(it.isSuccessful){
                    database.reference.child("users")
                        .child(userId)
                        .child("countInterstitialAds")
                        .setValue(0)
                        .addOnCompleteListener {
                            database.reference.child("users")
                                .child(userId)
                                .child("countInterstitialAdsClick")
                                .setValue(0)
                                .addOnCompleteListener {
                                    database.reference.child("users")
                                        .child(userId)
                                        .child("countRewardedAds")
                                        .setValue(0)
                                        .addOnCompleteListener {
                                            database.reference.child("users")
                                                .child(userId)
                                                .child("countRewardedAdsClick")
                                                .setValue(0)
                                                .addOnCompleteListener {
                                                    database.reference.child("users")
                                                        .child(userId)
                                                        .child("achievementPrice")
                                                        .setValue(0)
                                                        .addOnCompleteListener {
                                                            database.reference.child("users")
                                                                .child(userId)
                                                                .child("giftMoney")
                                                                .setValue(0)
                                                                .addOnCompleteListener {
                                                                    database.reference.child("users")
                                                                        .child(userId)
                                                                        .child("referralLinkMoney")
                                                                        .setValue(0)
                                                                        .addOnCompleteListener {
                                                                            if(activeReferralLink == null || !utils.referral_link){
                                                                                onCompleteListener(it)
                                                                            }else {
                                                                                database.reference.child("users")
                                                                                    .child(activeReferralLink.userId)
                                                                                    .child("referralLinkMoney")
                                                                                    .setValue((1.0 * sum) / 100)
                                                                                    .addOnCompleteListener {
                                                                                        onCompleteListener(it)
                                                                                    }
                                                                            }
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }else{
                    onCompleteListener(it)
                }
            }
    }

    fun deleteById(id:String, onSuccess: () -> Unit, onError:(message: String) -> Unit){
        database.reference.child("withdrawal_request").child(id).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Ошибка") }
    }

    fun updateStatus(
        id: String,
        status: WithdrawalRequestStatus,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit,
    ){
        database.reference.child("withdrawal_request")
            .child(id)
            .child("status")
            .setValue(status)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Ошибка") }
    }
}