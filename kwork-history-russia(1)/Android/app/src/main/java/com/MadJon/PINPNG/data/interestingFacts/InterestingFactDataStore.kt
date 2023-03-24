package com.MadJon.PINPNG.data.interestingFacts

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.MadJon.PINPNG.data.interestingFacts.model.InterestingFact
import com.MadJon.PINPNG.data.interestingFacts.model.mapInterestingFact
import com.MadJon.PINPNG.data.user.UserDataStore

class InterestingFactDataStore {

    private val db = Firebase.database
    private val userDataStore = UserDataStore()

    fun getRandomInterestingFact(
        userCountInterestingFact: Int,
        onSuccess:(InterestingFact) -> Unit
    ) {
        val randomId = (0..10).random()

        db.reference.child("interesting_fact").child(randomId.toString()) .get()
            .addOnSuccessListener {
                userDataStore.updateCountInterestingFact(userCountInterestingFact + 1)
                onSuccess(it.mapInterestingFact())
            }
    }
}