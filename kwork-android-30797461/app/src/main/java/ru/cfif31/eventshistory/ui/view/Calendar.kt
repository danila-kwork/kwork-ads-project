package ru.cfif31.eventshistory.ui.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.cfif31.eventshistory.R

@Composable
fun Calendar(
    date:String,
    modifier: Modifier = Modifier
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.calendar),
            contentDescription = null,
            modifier = modifier
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier
        ) {
            val height = if(date.count() < 20) 20.dp else 40.dp
            val textSize = if(date.count() < 20) 14.sp else 12.sp

            Log.e("Calendar",height.toString())
            Log.e("Calendar",date.count().toString())

            Spacer(modifier = Modifier.height(height))

            Text(
                text = date,
                color = Color.Red,
                modifier = Modifier.width(110.dp),
                textAlign = TextAlign.Center,
                fontSize = textSize
            )
        }
    }
}