package com.lfybkf11701.watchads.yandexAds

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.lfybkf11701.watchads.data.firebase.utils.UtilsDataStore
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import java.time.LocalDateTime

class RewardedYandexAds(
    private val context:Context,
    private val onDismissed:(
        adClickedDate: LocalDateTime?,
        returnedToDate: LocalDateTime?,
        rewarded:Boolean
    ) -> Unit
): RewardedAdEventListener {

    private var rewardedAd:RewardedAd? = RewardedAd(context)
    private val utilsDataStore = UtilsDataStore()

    init {
        utilsDataStore.get(onSuccess = {
            configureRewardedAd(it.rewarded_yandex_ads_id.ifEmpty { AD_UNIT_ID })
        })
    }

    private var adClickedDate: LocalDateTime? = null
    private var returnedToDate: LocalDateTime? = null
    private var rewarded = false

    fun show() {
        adClickedDate = null
        returnedToDate = null
        rewarded = false

        loadRewardedAd()
    }

    private fun loadRewardedAd() = rewardedAd?.loadAd(AdRequest.Builder().build())

    private fun configureRewardedAd(adUnitId: String) {
        rewardedAd?.setAdUnitId(adUnitId)
        rewardedAd?.setRewardedAdEventListener(this)
    }

    private companion object { const val AD_UNIT_ID = "R-M-2193940-3" }

    override fun onAdLoaded() { rewardedAd?.show() }

    override fun onAdFailedToLoad(p0: AdRequestError) {
        Toast.makeText(context, p0.code.toString() + p0.description, Toast.LENGTH_SHORT).show()
        Log.e("onAdFailedToLoad",p0.code.toString() + p0.description)
    }

    override fun onAdShown() = Unit

    override fun onAdDismissed() {
        onDismissed(adClickedDate,returnedToDate, rewarded)
    }

    override fun onRewarded(p0: Reward) {
        rewarded = true
    }

    @SuppressLint("NewApi")
    override fun onAdClicked() {
        adClickedDate = LocalDateTime.now()
    }

    override fun onLeftApplication() = Unit

    @SuppressLint("NewApi")
    override fun onReturnedToApplication() {
        returnedToDate = LocalDateTime.now()
    }

    override fun onImpression(p0: ImpressionData?) = Unit

    fun destroy() {
        rewardedAd?.destroy()
        rewardedAd = null
    }
}