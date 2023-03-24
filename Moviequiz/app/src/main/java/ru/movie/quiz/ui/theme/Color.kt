package ru.movie.quiz.ui.theme

import androidx.compose.ui.graphics.Color

val primaryBackground = Color(0xFF181818)
val primaryText = Color(0xFFE0E0E0)
val secondaryBackground = Color(0xFF423E3E)
val tintColor = Color(0xFF4DB331)

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

fun Double.getRatingColor(): Color {
    return if(this < 5)
        Color(0xFFB31B1B)
    else if(this < 7)
        Color(0xFF302929)
    else
        Color(0xFF1B8A1D)
}