package sinelix.sportapp.ui.screens

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewNavigator
import com.google.accompanist.web.rememberWebViewState

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String
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
            it.webViewClient = WebViewClient()
            it.settings.javaScriptEnabled = true
            it.settings.domStorageEnabled = true
            it.settings.javaScriptCanOpenWindowsAutomatically = true

            val cookieManager = CookieManager.getInstance()
            cookieManager.setAcceptCookie(true)

            val webSettings = it.settings
            webSettings.javaScriptEnabled = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
            webSettings.domStorageEnabled = true
            webSettings.databaseEnabled = true
            webSettings.setSupportZoom(false)
            webSettings.allowFileAccess = true
            webSettings.allowContentAccess = true
            webSettings.loadWithOverviewMode = true
            webSettings.useWideViewPort = true
        }
    )
}