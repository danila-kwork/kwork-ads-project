package ru.thirdproject.population.common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun String.parseTime():String{
    return SimpleDateFormat("dd MMMM yyyy")
        .parseTime(this)
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("SimpleDateFormat")
private fun SimpleDateFormat.parseTime(string: String):String{
    return try {
        val format = SimpleDateFormat("dd MMMM yyyy")
        val time = format.parse(string)
        format(time)
    }catch (e:Exception){
        e.message ?: "error"
    }
}
