package ru.pgk63.guesstheword.yandexAds

import android.content.Context
import android.widget.Toast
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener

class RewardedYandexAds(
    private val context:Context,
    private val rewarded:(Reward) -> Unit = {}
): RewardedAdEventListener {

    private var rewardedAd:RewardedAd? = RewardedAd(context)

    init { configureRewardedAd() }

    fun show() = loadRewardedAd()

    private fun loadRewardedAd() = rewardedAd?.loadAd(AdRequest.Builder().build())

    private fun configureRewardedAd() {
        rewardedAd?.setAdUnitId(AD_UNIT_ID)
        rewardedAd?.setRewardedAdEventListener(this)
    }

    private companion object { const val AD_UNIT_ID = "R-M-2105198-1" }

    override fun onAdLoaded() { rewardedAd?.show() }

    override fun onAdFailedToLoad(p0: AdRequestError) {
        Toast.makeText(context, p0.code.toString() + p0.description, Toast.LENGTH_SHORT).show()
    }

    override fun onAdShown() = Unit

    override fun onAdDismissed() = Unit

    override fun onRewarded(p0: Reward) {
        rewarded(p0)
    }

    override fun onAdClicked() = Unit

    override fun onLeftApplication() = Unit

    override fun onReturnedToApplication() = Unit

    override fun onImpression(p0: ImpressionData?) = Unit

    fun destroy() {
        rewardedAd?.destroy()
        rewardedAd = null
    }
}