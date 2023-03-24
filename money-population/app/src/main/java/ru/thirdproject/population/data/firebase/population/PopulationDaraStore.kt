package ru.thirdproject.population.data.firebase.population

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import ru.thirdproject.population.data.firebase.population.model.PopulationQuestion
import ru.thirdproject.population.data.firebase.population.model.mapPopulationQuestion
import kotlin.random.Random

class PopulationDaraStore constructor(){

    private val db = Firebase.database

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