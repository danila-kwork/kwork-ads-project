package cfif31.kwork.namevalues

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cfif31.kwork.namevalues.navigation.StartNavHost
import dagger.hilt.android.AndroidEntryPoint
import cfif31.kwork.namevalues.ui.theme.MainTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContent {
            MainTheme(
                darkTheme = false
            ) {
                StartNavHost()
            }
        }
    }
}