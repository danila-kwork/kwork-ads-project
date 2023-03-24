package com.MadJon.PINPNG.ui.screens.adsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.MadJon.PINPNG.data.user.UserDataStore
import com.MadJon.PINPNG.data.user.model.User
import com.MadJon.PINPNG.yandexAds.InterstitialYandexAds
import com.MadJon.PINPNG.yandexAds.RewardedYandexAds
import org.joda.time.Period
import com.MadJon.PINPNG.R
import com.MadJon.PINPNG.data.user.model.userSumMoneyVersion2
import com.MadJon.PINPNG.data.utils.UtilsDataStore
import com.MadJon.PINPNG.data.utils.model.Utils
import com.MadJon.PINPNG.ui.view.BaseButton
import com.MadJon.PINPNG.ui.view.Board

@Composable
fun AdsScreen(

) {
    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    var utils by remember { mutableStateOf<Utils?>(null) }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            user ?: return@InterstitialYandexAds

            if(adClickedDate != null && returnedToDate != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountInterstitialAdsClick(user!!.countInterstitialAdsClick + 1)
                }else {
                    userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
                }
            } else {
                userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
            }
        })
    }

    val rewardedYandexAds = remember {
        RewardedYandexAds(context, onDismissed = { adClickedDate, returnedToDate, rewarded ->
            if(adClickedDate != null && returnedToDate != null && rewarded && user != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountRewardedAdsClick(user!!.countRewardedAdsClick + 1)
                }else {
                    userDataStore.updateCountRewardedAds(user!!.countRewardedAds + 1)
                }
            } else if(rewarded && user != null){
                userDataStore.updateCountRewardedAds(user!!.countRewardedAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

    Image(
        bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.main_background),
            screenWidthDp,
            screenHeightDp,
            false
        ).asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.size(
            width = screenWidthDp.dp,
            height = screenHeightDp.dp
        )
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Board(
                modifier = Modifier.padding(10.dp),
                text = userSumMoneyVersion2(
                    utils = utils,
                    countBannerAds = user?.countBannerAds ?: 0,
                    countBannerAdsClick = user?.countBannerAdsClick ?: 0,
                    countInterstitialAds = user?.countInterstitialAds ?: 0,
                    countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
                    countRewardedAds = user?.countRewardedAds ?: 0,
                    countRewardedAdsClick = user?.countRewardedAdsClick ?: 0,
                    achievementPrice = user?.achievementPrice ?: 0.0
                ).toString(),
                width = (screenWidthDp / 2).toDouble(),
                height = (screenHeightDp / 10).toDouble()
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            BaseButton(
                text = "Видио",
                onClick = {
                    rewardedYandexAds.show()
                }
            )

            BaseButton(
                text = "Полноэкранная",
                onClick = {
                    interstitialYandexAds.show()
                }
            )
        }
    }
}