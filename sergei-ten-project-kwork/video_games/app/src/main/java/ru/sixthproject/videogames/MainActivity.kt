package ru.sixthproject.videogames

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.sixthproject.videogames.navigation.StartNavHost
import dagger.hilt.android.AndroidEntryPoint
import ru.sixthproject.videogames.ui.theme.MainTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            MainTheme {
                StartNavHost()
            }
        }
    }
}