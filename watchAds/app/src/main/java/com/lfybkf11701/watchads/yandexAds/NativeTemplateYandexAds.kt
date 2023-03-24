package com.lfybkf11701.watchads.yandexAds

import android.content.Context
import android.util.Log
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.nativeads.*
import com.yandex.mobile.ads.nativeads.template.NativeBannerView

class NativeTemplateYandexAds(
    context: Context
): NativeAdLoadListener {

    private var nativeAdLoader: NativeAdLoader? = NativeAdLoader(context)
    val nativeBannerView = NativeBannerView(context)

    private companion object {
        const val AD_UNIT_ID = "R-M-2141855-2"

        val adFoxParameters = mapOf(
            "adf_ownerid" to "270901",
            "adf_p1" to "cqtgi",
            "adf_p2" to "fksh"
        )
    }

    init { loadNative() }

    private fun loadNative() {
        nativeAdLoader?.loadAd(
            NativeAdRequestConfiguration.Builder(AD_UNIT_ID)
                .setParameters(adFoxParameters)
                .setShouldLoadImagesAutomatically(true)
                .build()
        )

        nativeAdLoader?.setNativeAdLoadListener(this)
    }

    override fun onAdLoaded(nativeAd: NativeAd) {
        nativeBannerView.setAd(nativeAd)
    }

    override fun onAdFailedToLoad(error: AdRequestError) {
        Log.e("onAdFailedToLoad",error.code.toString() + error.description)
    }
}