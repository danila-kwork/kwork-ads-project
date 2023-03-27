package ru.madtwoproject3.common

import kotlin.math.pow
import kotlin.math.roundToInt

fun round(value: Double, places: Int): Double {
    var value1 = value
    require(places >= 0)
    val factor = 10.0.pow(places.toDouble()).toLong()
    value1 *= factor
    val tmp = value1.roundToInt()
    return tmp.toDouble() / factor
}