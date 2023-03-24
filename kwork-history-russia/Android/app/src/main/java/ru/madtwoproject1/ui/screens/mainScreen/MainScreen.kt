package ru.madtwoproject1.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.madtwoproject1.R
import ru.madtwoproject1.common.openBrowser
import ru.madtwoproject1.common.vpn
import ru.madtwoproject1.data.user.UserDataStore
import ru.madtwoproject1.data.user.model.User
import ru.madtwoproject1.data.user.model.UserRole
import ru.madtwoproject1.data.utils.UtilsDataStore
import ru.madtwoproject1.data.utils.model.Utils
import ru.madtwoproject1.data.withdrawalRequest.WithdrawalRequestDataStore
import ru.madtwoproject1.data.withdrawalRequest.model.WithdrawalRequest
import ru.madtwoproject1.ui.navigation.Screen
import ru.madtwoproject1.ui.screens.mainScreen.view.RewardAlertDialog
import ru.madtwoproject1.ui.view.BaseButton
import ru.madtwoproject1.ui.view.BaseLottieAnimation
import ru.madtwoproject1.ui.view.LottieAnimationType
import ru.madtwoproject1.ui.screens.mainScreen.view.GiftAlertDialog
import ru.madtwoproject1.ui.screens.mainScreen.view.InfoAlertDialog

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
    var giftAlertDialog by remember { mutableStateOf(false) }
    var infoAlertDialog by remember { mutableStateOf(false) }

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
            referralLinkMoney = user?.referralLinkMoney ?: 0.0,
            giftMoney = user?.giftMoney ?: 0.0,
            countInterstitialAds = user?.countInterstitialAds ?: 0,
            countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
            countRewardedAds = user?.countRewardedAds ?: 0,
            countRewardedAdsClick = user?.countRewardedAdsClick ?: 0,
            achievementPrice = user?.achievementPrice ?: 0.0,
            onDismissRequest = { rewardAlertDialog = false },
            onSendWithdrawalRequest = { phoneNumber ->
                user ?: return@RewardAlertDialog
                utils ?: return@RewardAlertDialog

                val withdrawalRequest = WithdrawalRequest(
                    countInterstitialAds = user!!.countInterstitialAds,
                    countInterstitialAdsClick = user!!.countInterstitialAdsClick,
                    countRewardedAds = user!!.countRewardedAds,
                    countRewardedAdsClick = user!!.countRewardedAdsClick,
                    phoneNumber = phoneNumber,
                    userEmail = user!!.email,
                    version = 1,
                    achievementPrice = user!!.achievementPrice,
                    vpn = vpn(),
                    referralLinkMoney = user!!.referralLinkMoney,
                    giftMoney = user!!.giftMoney
                )

                withdrawalRequestDataStore.create(
                    utils = utils!!,
                    activeReferralLink = user?.activeReferralLink,
                    withdrawalRequest = withdrawalRequest
                ) {
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


    if(giftAlertDialog) {
        GiftAlertDialog(
            onDismissRequest = { giftAlertDialog = false },
            onGift = { gift ->
                user?.giftMoney?.let {
                    userDataStore.updateGiftMoney(it + gift)
                }
            }
        )
    }

    if(infoAlertDialog){
        InfoAlertDialog(info = utils?.info ?: "") {
            infoAlertDialog = false
        }
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
                        .size(250.dp)
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
                    text = "Рефералка",
                    onClick = {
                        navController.navigate(Screen.ReferralLink.route)
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

                Row {
                    AnimatedVisibility(visible = user != null) {
                        IconButton(onClick = {
                            giftAlertDialog = true
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.gift),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(5.dp)
                            )
                        }
                    }

                    IconButton(onClick = {
                        context.openBrowser("https://t.me/+MH4kzXnOzWNlYzNi")
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.telegram),
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(5.dp)
                        )
                    }

                    AnimatedVisibility(visible = utils != null && utils!!.info.isNotEmpty()) {
                        IconButton(onClick = {
                            infoAlertDialog = true
                        }) {
                            Image(
                                painter = painterResource(id = R.drawable.info),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .padding(5.dp)
                            )
                        }
                    }
                }
            }
        }

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4479af)))
    }
}