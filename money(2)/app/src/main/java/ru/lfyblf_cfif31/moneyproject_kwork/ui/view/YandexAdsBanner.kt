package ru.lfyblf_cfif31.moneyproject_kwork.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest

private const val AD_UNIT_ID = "R-M-2153074-1"

@Composable
fun YandexAdsBanner(
    modifier: Modifier = Modifier,
    size: AdSize = AdSize.BANNER_400x240,
    adUnitId: String
) {
    AndroidView(
        modifier = modifier,
        factory = {
            BannerAdView(it).apply {
                setAdUnitId(adUnitId)
                setAdSize(size)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}