package ru.money_ads.moneyproject_kwork.ui.view

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.AdSize
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import ru.money_ads.moneyproject_kwork.data.firebase.user.model.User
import java.time.Duration
import java.time.LocalDateTime

private const val AD_UNIT_ID = "R-M-2153074-1"

@Composable
fun YandexAdsBanner(
    modifier: Modifier = Modifier,
    size: AdSize = AdSize.BANNER_400x240,
    adUnitId: String,
    returnedToApplication: (
        adClickedDate: LocalDateTime?,
        returnedToDate: LocalDateTime?
    ) -> Unit = { _,_ -> }
) {
    var adClickedDate by remember { mutableStateOf<LocalDateTime?>(null) }
    var returnedToDate by remember { mutableStateOf<LocalDateTime?>(null) }

    AndroidView(
        modifier = modifier,
        factory = {
            BannerAdView(it).apply {
                setAdUnitId(adUnitId.ifEmpty { AD_UNIT_ID })
                setAdSize(size)
                loadAd(AdRequest.Builder().build())
                setBannerAdEventListener(object : BannerAdEventListener {
                    override fun onAdLoaded() {
                        adClickedDate = null
                        returnedToDate = null
                    }

                    override fun onAdFailedToLoad(p0: AdRequestError) {

                    }

                    @SuppressLint("NewApi")
                    override fun onAdClicked() {
                        adClickedDate = LocalDateTime.now()
                    }

                    override fun onLeftApplication() {

                    }

                    @SuppressLint("NewApi")
                    override fun onReturnedToApplication() {
                        returnedToDate = LocalDateTime.now()
                        returnedToApplication(adClickedDate, returnedToDate)
                    }

                    override fun onImpression(p0: ImpressionData?) {

                    }
                })
            }
        }
    )
}

@SuppressLint("NewApi")
fun addCountWatchBanner(
    adClickedDate: LocalDateTime?,
    returnedToDate: LocalDateTime?,
    user: User,
    updateCountBannerAdsClick: (Int) -> Unit,
    updateCountBannerAds: (Int) -> Unit,
) {
    if(adClickedDate != null && returnedToDate != null){
        if((Duration.between(adClickedDate, returnedToDate)).seconds >= 10){
            updateCountBannerAdsClick(user.countInterstitialAdsClick + 1)
        }else {
            updateCountBannerAds(user.countInterstitialAds + 1)
        }
    } else {
        updateCountBannerAds(user.countInterstitialAds + 1)
    }
}