package en.cfif33.answer

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import en.cfif33.answer.navigation.StartNavHost
import dagger.hilt.android.AndroidEntryPoint
import en.cfif33.answer.ui.theme.MainTheme

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