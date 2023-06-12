package ru.lfyblf_cfif31.moneyproject_kwork.ui.view

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.admanager.AppEventListener
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData

private const val AD_UNIT_ID = "R-M-2153074-1"

@Composable
fun YandexAdsBanner(
    modifier: Modifier = Modifier,
    size: AdSize = AdSize.BANNER_320x50,
    adUnitId: String
) {
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = {
            BannerAdView(it).apply {
                setAdUnitId(adUnitId)
                setAdSize(size)
                loadAd(AdRequest.Builder().build())
                setBannerAdEventListener(object : BannerAdEventListener {
                    override fun onAdLoaded() {

                    }

                    override fun onAdFailedToLoad(p0: AdRequestError) {
                        Log.e("onAdFailedToLoad", p0.description)
//                        Toast.makeText(context, p0.description, Toast.LENGTH_SHORT).show()
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