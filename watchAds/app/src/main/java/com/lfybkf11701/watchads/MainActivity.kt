package com.lfybkf11701.watchads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lfybkf11701.watchads.ui.screens.MainScreen
import com.lfybkf11701.watchads.ui.theme.WatchAdsTheme
import com.lfybkf11701.watchads.ui.view.YandexAdsBanner

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchAdsTheme {
                MainScreen()

//                val count = remember { mutableStateOf(0) }
//
//                Column(
//                    modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Text(
//                        text = count.value.toString(),
//                        color = Color.Red,
//                        fontWeight = FontWeight.W900,
//                        fontSize = 32.sp,
//                        textAlign = TextAlign.Center,
//                        modifier = Modifier.padding(10.dp)
//                    )
//
//                    Image(
//                        painterResource(id = R.drawable.right_click),
//                        contentDescription = null,
//                        modifier = Modifier.size(120.dp).clickable {
//                            count.value++
//                        }
//                    )
//
//                    YandexAdsBanner(
//                        modifier = Modifier.padding(5.dp)
//                    )
//                }
            }
        }
    }
}