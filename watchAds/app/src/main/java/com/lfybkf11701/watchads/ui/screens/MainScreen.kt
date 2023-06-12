package com.lfybkf11701.watchads.ui.screens

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.lfybkf11701.watchads.ui.view.OnLifecycleEvent
import com.lfybkf11701.watchads.R
import com.lfybkf11701.watchads.common.Utils.checkVpn
import com.lfybkf11701.watchads.common.Utils.getAndroidVersion
import com.lfybkf11701.watchads.common.Utils.getDeviceName
import com.lfybkf11701.watchads.common.Utils.getIPAddress
import com.lfybkf11701.watchads.common.Utils.getMacAddress
import com.lfybkf11701.watchads.common.Utils.isProbablyRunningOnEmulator
import com.lfybkf11701.watchads.data.firebase.user.model.UserRole
import com.lfybkf11701.watchads.data.firebase.user.model.createUserLoading
import com.lfybkf11701.watchads.data.firebase.user.model.userSumMoneyVersion2
import com.lfybkf11701.watchads.data.firebase.utils.UtilsDataStore
import com.lfybkf11701.watchads.data.firebase.utils.model.Utils
import com.lfybkf11701.watchads.data.firebase.withdrawalRequest.model.WithdrawalRequest
import com.lfybkf11701.watchads.ui.theme.primaryText
import com.lfybkf11701.watchads.ui.view.YandexAdsBanner
import com.lfybkf11701.watchads.ui.view.addCountBannerWatch
import com.lfybkf11701.watchads.yandexAds.InterstitialYandexAds
import com.lfybkf11701.watchads.yandexAds.RewardedYandexAds
import kotlinx.coroutines.launch
import java.time.Duration

@SuppressLint("NewApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navController: NavController
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var user by remember { mutableStateOf(createUserLoading()) }

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)

    var rewardAlertDialog by remember { mutableStateOf(false) }

    val rewardedYandexAds = remember {
        RewardedYandexAds(context, onDismissed = { adClickedDate, returnedToDate, rewarded ->
            if(adClickedDate != null && returnedToDate != null && rewarded){
                if((Duration.between(adClickedDate, returnedToDate)).seconds >= 10){
                    viewModel.updateCountRewardedAdsClick(user.countRewardedAdsClick + 1)
                }else {
                    viewModel.updateCountRewardedAds(user.countRewardedAds + 1)
                }
            } else if(rewarded){
                viewModel.updateCountRewardedAds(user.countRewardedAds + 1)
            }
        })
    }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            scope.launch {
                if(adClickedDate != null && returnedToDate != null){
                    if((Duration.between(adClickedDate, returnedToDate)).seconds >= 10){
                        viewModel.updateCountInterstitialAdsClick(user.countInterstitialAdsClick + 1)
                    }else {
                        viewModel.updateCountInterstitialAds(user.countInterstitialAds + 1)
                    }
                } else {
                    viewModel.updateCountInterstitialAds(user.countInterstitialAds + 1)
                }
            }
        })
    }


    OnLifecycleEvent { _, event ->
        if(event == Lifecycle.Event.ON_DESTROY){
            rewardedYandexAds.destroy()
            interstitialYandexAds.destroy()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getUser { user = it }

        utilsDataStore.get(
            onSuccess = { utils = it }
        )
    })

    if(rewardAlertDialog){
        RewardAlertDialog(
            utils = utils,
            countBannerAdsClick = user.countBannerAdsClick,
            countBannerAds = user.countBannerAds,
            countInterstitialAds = user.countInterstitialAds,
            countInterstitialAdsClick = user.countInterstitialAdsClick,
            countRewardedAds = user.countRewardedAds,
            countRewardedAdsClick = user.countRewardedAdsClick,
            onDismissRequest = {
                rewardAlertDialog = false
            },
            onSendWithdrawalRequest = { phoneNumber ->
                val withdrawalRequest = WithdrawalRequest(
                    countInterstitialAds = user.countInterstitialAds,
                    countInterstitialAdsClick = user.countInterstitialAdsClick,
                    countRewardedAds = user.countRewardedAds,
                    countRewardedAdsClick = user.countRewardedAdsClick,
                    countBannerAds = user.countBannerAds,
                    countBannerAdsClick = user.countBannerAdsClick,
                    phoneNumber = phoneNumber,
                    userEmail = user.email,
                    version = 2,
                    macAddress = getMacAddress(context),
                    checkEmulator = isProbablyRunningOnEmulator,
                    checkVpn = checkVpn(),
                    iPv4 = getIPAddress(true),
                    iPv6 = getIPAddress(false),
                    deviceName = getDeviceName(),
                    androidVersion = getAndroidVersion()
                )

                viewModel.sendWithdrawalRequest(withdrawalRequest,{
                    rewardAlertDialog = false
                    Toast.makeText(context, "Заявка отправлена", Toast.LENGTH_SHORT).show()
                },{ message ->
                    Toast.makeText(context, "Ошибка: $message", Toast.LENGTH_SHORT).show()
                })
            }
        )
    }

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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

//        YandexAdsNativeBanner()

//        Spacer(modifier = Modifier.height(30.dp))

        Board(
            text = if(utils == null)
                ""
            else
                userSumMoneyVersion2(
                    utils = utils!!,
                    countInterstitialAds = user.countInterstitialAds,
                    countInterstitialAdsClick = user.countInterstitialAdsClick,
                    countRewardedAds = user.countRewardedAds,
                    countRewardedAdsClick = user.countRewardedAdsClick,
                    countBannerAds = user.countBannerAds,
                    countBannerAdsClick = user.countBannerAdsClick
                ).toString()
        )


        Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

        MainButton(text = "Смотреть") {
            rewardedYandexAds.show()
        }

        MainButton(text = "Нажать") {
            interstitialYandexAds.show()
        }

        MainButton(text = "Награда") {
            rewardAlertDialog = true
        }

        if(user.userRole == UserRole.ADMIN){
            MainButton(text = "Админ"){
                navController.navigate("WithdrawalRequests")
            }
        }

        utils?.let {
            YandexAdsBanner(
                adUnitId = it.banner_yandex_ads_id,
                returnedToApplication = { adClickedDate, returnedToDate ->
                    addCountBannerWatch(
                        adClickedDate = adClickedDate,
                        returnedToDate = returnedToDate,
                        user = user,
                        updateCountBannerAdsClick = {
                            viewModel.updateCountBannerAdsClick(it)
                        },
                        updateCountBannerAds = {
                            viewModel.updateCountBannerAds(it)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun MainButton(
    text:String,
    onClick: () -> Unit
) {

    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Box {
        Image(
            bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources,R.drawable.main_button),
                (screenWidthDp / 2.4).toInt(),
                (screenHeightDp / 13),
                false
            ).asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .padding(
                    5.dp
                )
                .height((screenHeightDp / 13).dp)
                .width((screenWidthDp / 2.4).dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(
                    5.dp
                )
                .height((screenHeightDp / 13).dp)
                .width((screenWidthDp / 2.4).dp)
                .clickable { onClick() }
        ) {
            Text(
                text = text,
                color = Color.Yellow,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center
            )
        }

    }
}

@Composable
private fun Board(text: String) {
    Text(
        text = "$text ₽",
        fontSize = 35.sp,
        fontWeight = FontWeight.W900,
        modifier = Modifier.padding(10.dp),
        color = primaryText,
        textAlign = TextAlign.Center
    )
}