package ru.fourthproject.guessthenumber.data.firebase.number

import ru.fourthproject.guessthenumber.data.firebase.number.model.NumberQuestion

class NumberDaraStore {

    fun getNumberRandom(): NumberQuestion{
        val number = (1..100).random()

        return NumberQuestion(
            question = "Я загатал число от 1 до 100\nПопробуй отгадать",
            answer = number.toString()
        )
    }
}