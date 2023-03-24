package ru.seventhproject.ridde

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.seventhproject.ridde.navigation.StartNavHost
import dagger.hilt.android.AndroidEntryPoint
import ru.seventhproject.ridde.ui.theme.MainTheme

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