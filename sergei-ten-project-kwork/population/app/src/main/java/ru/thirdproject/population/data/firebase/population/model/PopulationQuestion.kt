package ru.thirdproject.population.data.firebase.population.model

import com.google.firebase.database.DataSnapshot

data class PopulationQuestion(
    val question:String,
    val answer:String
)

fun createPopulationQuestionLoading(): PopulationQuestion {
    return PopulationQuestion(
        question = "Загрузка",
        answer = "Загрузка",
    )
}

fun DataSnapshot.mapPopulationQuestion(): PopulationQuestion {

    val population = this.child("population").value.toString()
    val city = this.child("name").value.toString()

    return  PopulationQuestion(
        question = "Какое насиления в городе $city ?",
        answer = population
    )
}