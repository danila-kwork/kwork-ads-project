package com.example.notes.ui.view

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

private const val AD_UNIT_ID = "R-M-2270271-1"

@Composable
fun YandexAdsBanner(
    modifier: Modifier = Modifier,
    adUnitId: String,
    size: AdSize = AdSize.BANNER_320x50 // BANNER_728x90
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = {
            BannerAdView(it).apply {
                setAdUnitId(adUnitId.ifEmpty { AD_UNIT_ID })
                setAdSize(size)
                loadAd(AdRequest.Builder().build())
                this.setBannerAdEventListener(object : BannerAdEventListener {
                    override fun onAdLoaded() {

                    }

                    override fun onAdFailedToLoad(p0: AdRequestError) {
//                        Toast.makeText(context, p0.description.toString(), Toast.LENGTH_SHORT).show()
                    }

                    override fun onAdClicked() {

                    }

                    override fun onLeftApplication() {

                    }

                    override fun onReturnedToApplication() {

                    }

                    override fun onImpression(p0: ImpressionData?) {

                    }
                })
            }
        }
    )
}