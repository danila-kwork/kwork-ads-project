package cfif69.kworkproject.morsecode

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import cfif69.kworkproject.morsecode.navigation.StartNavHost
import cfif69.kworkproject.morsecode.ui.theme.MainTheme

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