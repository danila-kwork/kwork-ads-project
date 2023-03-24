package ru.thirdproject.population.data.firebase.population

import ru.thirdproject.population.data.firebase.population.model.PopulationQuestion
import com.google.firebase.database.FirebaseDatabase
import ru.thirdproject.population.data.firebase.population.model.mapPopulationQuestion
import javax.inject.Inject
import kotlin.random.Random

class PopulationDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getPopulationRandom(
        onSuccess:(PopulationQuestion) -> Unit = {},
        onFailure:(message:String) -> Unit = {}
    ) {
        val id = Random.nextInt(0,1131).toString()

        db.reference.child("russian_cities").child(id).get()
            .addOnSuccessListener { onSuccess(it.mapPopulationQuestion()) }
            .addOnFailureListener { onFailure(it.message ?: "error") }
    }
}