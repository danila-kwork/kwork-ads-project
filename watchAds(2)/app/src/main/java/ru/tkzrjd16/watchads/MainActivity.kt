package ru.tkzrjd16.watchads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.tkzrjd16.watchads.ui.screens.MainScreen
import ru.tkzrjd16.watchads.ui.theme.WatchAdsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WatchAdsTheme {
                MainScreen()
            }
        }
    }
}