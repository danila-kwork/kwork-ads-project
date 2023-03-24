package ru.movie.quiz.data.interestingFacts

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.movie.quiz.common.RetrofitImpl
import ru.movie.quiz.data.interestingFacts.model.InterestingFact
import ru.movie.quiz.data.interestingFacts.model.InterestingFactResponse
import ru.movie.quiz.data.interestingFacts.model.mapInterestingFact
import ru.movie.quiz.data.user.UserDataStore

class InterestingFactDataStore {

    private val db = Firebase.database
    private val userDataStore = UserDataStore()
    private val filmApi = RetrofitImpl.filmsApi

    fun getInterestingFact(
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

    suspend fun getInterestingFact(
        filmId: Int
    ): InterestingFactResponse = filmApi.getFilmFacts(filmId)
}