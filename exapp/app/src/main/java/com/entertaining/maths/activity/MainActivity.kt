package com.entertaining.maths.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.entertaining.maths.fragments.home.HomeFragment
import com.entertaining.maths.R
import com.entertaining.maths.databinding.ActivityMainBinding
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    val viewModel by viewModels<MainViewModel>()

    private val navController by lazy {
        findNavController(R.id.container)
    }

    private lateinit var binding: ActivityMainBinding

    private var rewardAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        //HwAds.init(this)
        loadRewardAd()
    }

    private fun loadRewardAd() {
        if (rewardAd == null) {
            rewardAd = RewardedAd(this).apply { setAdUnitId(AD_ID) }
        }
        rewardAd!!.loadAd(AdRequest.Builder().build())
    }

    fun showRewardAd(fragment: HomeFragment) {
        if (rewardAd!!.isLoaded) {
            rewardAd!!.setRewardedAdEventListener(object : RewardedAdEventListener {

                override fun onReturnedToApplication() {
                    Timber.d("onReturnedToApplication")
                }

                override fun onImpression(p0: ImpressionData?) {
                    Timber.d("onImpression")
                }

                override fun onAdLoaded() {
                    Timber.d("onAdLoaded")
                }

                override fun onAdFailedToLoad(p0: AdRequestError) {
                    Timber.d("onAdFailedToLoad")
                }

                override fun onAdShown() {
                    Timber.d("onAdShown")
                }

                override fun onAdDismissed() {
                    loadRewardAd()
                }

                override fun onRewarded(p0: Reward) {
                    fragment.viewModel.onSuccessfulAd()
                }

                override fun onAdClicked() {

                }

                override fun onLeftApplication() {

                }
            })
            rewardAd!!.show()
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.homeFragment) {
            navController.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        @Suppress("SpellCheckingInspection")
        private const val AD_ID = "R-M-1667338-1"
    }
}
