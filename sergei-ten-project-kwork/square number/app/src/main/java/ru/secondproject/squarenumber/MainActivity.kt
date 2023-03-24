package ru.secondproject.squarenumber

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.secondproject.squarenumber.navigation.StartNavHost
import dagger.hilt.android.AndroidEntryPoint
import ru.secondproject.squarenumber.ui.theme.MainTheme

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