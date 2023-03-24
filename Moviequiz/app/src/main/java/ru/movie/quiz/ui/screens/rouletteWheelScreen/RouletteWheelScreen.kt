package ru.movie.quiz.ui.screens.rouletteWheelScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import ru.movie.quiz.ui.view.RouletteWheelView
import java.util.Random

@Composable
fun RouletteWheelScreen(

) {
    val context = LocalContext.current

    val rouletteWheelView = remember { RouletteWheelView(context) }
    val rand = remember { Random() }
    var result by remember { mutableStateOf(0f) }

    Column {
        AndroidView(factory = {
            rouletteWheelView
        })

        Button(onClick = {
            result = (rand.nextInt(360 - 0 + 1) + 0).toFloat()
            rouletteWheelView.spin(result)
        }) {
            Text(text = rouletteWheelView.mBallRollInnerDistance.toString())
        }
    }
}