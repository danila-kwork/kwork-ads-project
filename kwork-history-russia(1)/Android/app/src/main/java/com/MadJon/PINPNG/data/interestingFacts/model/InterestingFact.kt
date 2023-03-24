package com.MadJon.PINPNG.data.interestingFacts.model

import com.google.firebase.database.DataSnapshot

data class InterestingFact(
    val text: String
)

fun DataSnapshot.mapInterestingFact(): InterestingFact {
    return InterestingFact(
        text = child("text").value.toString()
    )
}