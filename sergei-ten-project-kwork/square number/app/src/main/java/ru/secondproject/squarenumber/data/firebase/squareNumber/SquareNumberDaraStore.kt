package ru.secondproject.squarenumber.data.firebase.squareNumber

import ru.secondproject.squarenumber.data.firebase.squareNumber.model.SquareNumberQuestion
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class SquareNumberDaraStore @Inject constructor(
    private val db:FirebaseDatabase
){

    fun getSquareNumberRandom(): SquareNumberQuestion{
        val number = (0..100).random()

        return SquareNumberQuestion(
            question = "Сколько будет $number в квадрате ?",
            answer = (number * number).toString()
        )
    }
}