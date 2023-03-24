package ru.fourthproject.guessthenumber.yandexAds

import android.content.Context
import android.widget.Toast
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import ru.fourthproject.guessthenumber.data.firebase.utils.UtilsDataStore

class RewardedYandexAds(
    private val context:Context
): RewardedAdEventListener {

    private var rewardedAd:RewardedAd? = RewardedAd(context)
    private val utilsDataStore = UtilsDataStore()

    init {
        utilsDataStore.get(onSuccess = {
            configureRewardedAd(it.rewarded_yandex_ads_id.ifEmpty { AD_UNIT_ID })
        })
    }

    fun show() = loadRewardedAd()

    private fun loadRewardedAd() = rewardedAd?.loadAd(AdRequest.Builder().build())

    private fun configureRewardedAd(adsId: String) {
        rewardedAd?.setAdUnitId(adsId)
        rewardedAd?.setRewardedAdEventListener(this)
    }

    private companion object { const val AD_UNIT_ID = "R-M-2146828-1" }

    override fun onAdLoaded() { rewardedAd?.show() }

    override fun onAdFailedToLoad(p0: AdRequestError) {
        Toast.makeText(context, p0.code.toString() + p0.description, Toast.LENGTH_SHORT).show()
    }

    override fun onAdShown() = Unit

    override fun onAdDismissed() = Unit

    override fun onRewarded(p0: Reward) = Unit

    override fun onAdClicked() = Unit

    override fun onLeftApplication() = Unit

    override fun onReturnedToApplication() = Unit

    override fun onImpression(p0: ImpressionData?) = Unit

    fun destroy() {
        rewardedAd?.destroy()
        rewardedAd = null
    }
}