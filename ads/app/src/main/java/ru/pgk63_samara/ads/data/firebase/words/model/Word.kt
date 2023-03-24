package ru.pgk63_samara.ads.data.firebase.words.model

import com.google.firebase.database.DataSnapshot
import kotlin.random.Random

data class Word(
    val id:Int,
    val word:String,
    val symbol:String
)

fun createWordLoading(): Word {
    return Word(
        id = 0,
        word = "Загрузка",
        symbol = ""
    )
}

fun DataSnapshot.mapWord(): Word {

    var word = this.child("word").value.toString()

    val symbol = word[Random.nextInt(0,word.length-1)].toString()

    word = word.replace(symbol,"*")

    return Word(
        id = this.child("id").value.toString().toInt(),
        word = word,
        symbol = symbol
    )
}