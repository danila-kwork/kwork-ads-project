package copa.gol.bet.app.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.AccompanistWebChromeClient
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    chromeClient: AccompanistWebChromeClient
) {
    val saveableUrl by rememberSaveable { mutableStateOf(url) }
    val webViewState = rememberWebViewState(saveableUrl)
    val navigation = rememberWebViewNavigator()

    BackHandler {
        if(navigation.canGoBack)
            navigation.navigateBack()
    }

    WebView(
        modifier = Modifier.fillMaxSize(),
        state = webViewState,
        onCreated = {
            it.settings.apply {
                javaScriptEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                domStorageEnabled = true
                databaseEnabled = true
                setSupportZoom(true)
                allowFileAccess = true
                allowContentAccess = true

                domStorageEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }
            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)
        },
        chromeClient = chromeClient
    )
}