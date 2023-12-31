package ru.pgk63.guesstheword.data.firebase.withdrawalRequest

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import ru.pgk63.guesstheword.data.firebase.withdrawalRequest.model.WithdrawalRequest
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

    fun create(withdrawalRequest: WithdrawalRequest, onCompleteListener:(Task<Void>) -> Unit = {}) {
        val id = UUID.randomUUID().toString()

        withdrawalRequest.id = id
        withdrawalRequest.userId = userId

        database.reference.child("withdrawal_request").child(id)
            .updateChildren(withdrawalRequest.dataMap())
            .addOnCompleteListener { it ->
                if(it.isSuccessful){
                    database.reference.child("users")
                        .child(userId)
                        .child("countAds")
                        .setValue(0)
                        .addOnCompleteListener {
                            database.reference.child("users")
                                .child(userId)
                                .child("countAnswers")
                                .setValue(0)
                                .addOnCompleteListener {
                                    onCompleteListener(it)
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
}