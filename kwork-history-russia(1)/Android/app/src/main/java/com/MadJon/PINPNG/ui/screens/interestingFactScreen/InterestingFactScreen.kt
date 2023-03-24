package com.MadJon.PINPNG.ui.screens.interestingFactScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.joda.time.Period
import com.MadJon.PINPNG.R
import com.MadJon.PINPNG.data.interestingFacts.InterestingFactDataStore
import com.MadJon.PINPNG.data.interestingFacts.model.InterestingFact
import com.MadJon.PINPNG.data.user.UserDataStore
import com.MadJon.PINPNG.data.user.model.User
import com.MadJon.PINPNG.data.user.model.userSumMoneyVersion2
import com.MadJon.PINPNG.data.utils.UtilsDataStore
import com.MadJon.PINPNG.data.utils.model.Utils
import com.MadJon.PINPNG.ui.theme.primaryText
import com.MadJon.PINPNG.ui.view.Board
import com.MadJon.PINPNG.ui.view.LoadingUi
import com.MadJon.PINPNG.yandexAds.RewardedYandexAds

@Composable
fun InterestingFactScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var user by remember { mutableStateOf<User?>(null) }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    val interestingFactDataStore = remember(::InterestingFactDataStore)
    var interestingFact by remember { mutableStateOf<InterestingFact?>(null) }
    var countInterestingFact by remember { mutableStateOf(0) }

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

    LaunchedEffect(key1 = countInterestingFact, block = {
        if(countInterestingFact % 5 == 0 && countInterestingFact != 0){
            rewardedYandexAds.show()
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        delay(1000L)
        user?.countInterestingFact?.let {
            interestingFactDataStore.getRandomInterestingFact(
                it
            ) { interestingFact = it; countInterestingFact++ }
        }
    })

    Image(
        bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.fonstola),
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

    if(interestingFact == null){
        LoadingUi()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = interestingFact?.text ?: "",
            fontWeight = FontWeight.W900,
            modifier = Modifier.padding(10.dp),
            color = primaryText,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ) {
        IconButton(onClick = { navController.navigateUp() }) {
            Image(
                painter = painterResource(id = R.drawable.left),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }

        IconButton(onClick = {
            user?.countInterestingFact?.let {
                interestingFact = null

                interestingFactDataStore.getRandomInterestingFact(
                    it
                ) {
                    interestingFact = it
                    countInterestingFact++
                }
            }
        }) {
            Image(
                painter = painterResource(id = R.drawable.right),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
        }
    }
}