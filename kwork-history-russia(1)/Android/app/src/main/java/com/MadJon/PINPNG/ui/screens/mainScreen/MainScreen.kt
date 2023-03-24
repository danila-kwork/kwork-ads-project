package com.MadJon.PINPNG.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.MadJon.PINPNG.R
import com.MadJon.PINPNG.common.vpn
import com.MadJon.PINPNG.data.user.UserDataStore
import com.MadJon.PINPNG.data.user.model.User
import com.MadJon.PINPNG.data.user.model.UserRole
import com.MadJon.PINPNG.data.utils.UtilsDataStore
import com.MadJon.PINPNG.data.utils.model.Utils
import com.MadJon.PINPNG.data.withdrawalRequest.WithdrawalRequestDataStore
import com.MadJon.PINPNG.data.withdrawalRequest.model.WithdrawalRequest
import com.MadJon.PINPNG.ui.navigation.Screen
import com.MadJon.PINPNG.ui.screens.mainScreen.view.RewardAlertDialog
import com.MadJon.PINPNG.ui.view.BaseButton
import com.MadJon.PINPNG.ui.view.BaseLottieAnimation
import com.MadJon.PINPNG.ui.view.LottieAnimationType

@Composable
fun MainScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    val withdrawalRequestDataStore = remember(::WithdrawalRequestDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    var utils by remember { mutableStateOf<Utils?>(null) }
    var rewardAlertDialog by remember { mutableStateOf(false) }

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

    if(rewardAlertDialog){
        RewardAlertDialog(
            utils = utils,
            countInterstitialAds = user?.countInterstitialAds ?: 0,
            countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
            countRewardedAds = user?.countRewardedAds ?: 0,
            countRewardedAdsClick = user?.countRewardedAdsClick ?: 0,
            achievementPrice = user?.achievementPrice ?: 0.0,
            onDismissRequest = { rewardAlertDialog = false },
            onSendWithdrawalRequest = { phoneNumber ->
                user ?: return@RewardAlertDialog

                val withdrawalRequest = WithdrawalRequest(
                    countInterstitialAds = user!!.countInterstitialAds,
                    countInterstitialAdsClick = user!!.countInterstitialAdsClick,
                    countRewardedAds = user!!.countRewardedAds,
                    countRewardedAdsClick = user!!.countRewardedAdsClick,
                    phoneNumber = phoneNumber,
                    userEmail = user!!.email,
                    version = 1,
                    achievementPrice = user!!.achievementPrice,
                    vpn = vpn()
                )

                withdrawalRequestDataStore.create(withdrawalRequest) {
                    if (it.isSuccessful) {
                        rewardAlertDialog = false
                        Toast.makeText(context, "Заявка отправлена", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Ошибка: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }

    Column {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.size(
                width = screenWidthDp.dp,
                height = screenHeightDp.dp
            )
        ) {
            item {
                BaseLottieAnimation(
                    type = LottieAnimationType.LANGUAGE,
                    modifier = Modifier
                        .size(350.dp)
                        .padding(5.dp)
                )

                BaseButton(
                    text = "Интересные\nфакты",
                    onClick = {
                        navController.navigate(Screen.InterestingFact.route)
                    }
                )

                BaseButton(
                    text = "Вопросы",
                    onClick = {
                        navController.navigate(Screen.Questions.route)
                    }
                )

                BaseButton(
                    text = "Реклама",
                    onClick = {
                        navController.navigate(Screen.Ads.route)
                    }
                )

                BaseButton(
                    text = "Награда",
                    onClick = {
                        rewardAlertDialog = true
                    }
                )

                BaseButton(
                    text = "Достижения",
                    onClick = {
                        navController.navigate(Screen.Achievement.route)
                    }
                )

                if(user?.userRole == UserRole.ADMIN){
                    BaseButton(
                        text = "Админ",
                        onClick = {
                            navController.navigate(Screen.Admin.route)
                        }
                    )
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4479af)))
    }
}