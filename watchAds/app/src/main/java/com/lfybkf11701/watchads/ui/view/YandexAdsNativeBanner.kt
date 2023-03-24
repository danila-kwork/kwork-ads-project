package com.lfybkf11701.watchads.ui.view

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.lfybkf11701.watchads.yandexAds.NativeTemplateYandexAds

@Composable
fun YandexAdsNativeBanner(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val nativeTemplateYandexAds = remember { NativeTemplateYandexAds(context) }

    AndroidView(
        modifier = modifier,
        factory = {
            nativeTemplateYandexAds.nativeBannerView.apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            }
        }
    )
}