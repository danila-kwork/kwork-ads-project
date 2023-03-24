package ru.movie.quiz.data.interestingFacts.model

import com.google.firebase.database.DataSnapshot

data class InterestingFactResponse(
    val total:Int,
    val items: List<InterestingFact>
)

data class InterestingFact(
    val text: String
)

fun DataSnapshot.mapInterestingFact(): InterestingFact {
    return InterestingFact(
        text = child("text").value.toString()
    )
}