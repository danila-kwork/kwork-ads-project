package ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.ui.screens.mainScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.CountDownTimer
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yandex.mobile.ads.banner.AdSize
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.LocalNavController
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.R
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.data.firebase.user.model.UserRole
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.data.firebase.user.model.createUserLoading
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.data.firebase.user.model.userSumMoneyVersion2
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.data.firebase.utils.UtilsDataStore
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.data.firebase.utils.model.Utils
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.data.firebase.withdrawalRequest.model.WithdrawalRequest
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.navigation.Screen
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.ui.screens.mainScreen.view.RewardAlertDialog
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.ui.theme.primaryText
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.ui.view.OnLifecycleEvent
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.ui.view.SchoolBoard
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.ui.view.YandexAdsBanner
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.yandexAds.InterstitialYandexAds
import ru.lfyblf_cfif_tkzrjd_31_19.moneyproject_kwork.yandexAds.RewardedYandexAds
import java.time.Duration

@SuppressLint("NewApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val navController = LocalNavController.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var user by remember { mutableStateOf(createUserLoading()) }

    var rewardAlertDialog by remember { mutableStateOf(false) }
    var buttonVisibility by remember { mutableStateOf(false) }
    var adsVisibility by remember { mutableStateOf(true) }
    var timerButtonVisibilityTickSecond by remember { mutableStateOf(0L) }

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)

    val timerButtonVisibility = object: CountDownTimer(15000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            timerButtonVisibilityTickSecond = millisUntilFinished / 1000
        }

        override fun onFinish() {
            buttonVisibility = true
        }
    }

    val timerAdsVisibility = object: CountDownTimer(15000, 1000) {
        override fun onTick(millisUntilFinished: Long) = Unit

        override fun onFinish() {
            adsVisibility = true
        }
    }

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

            adsVisibility = false
            timerAdsVisibility.start()
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

                delay(500L)
                rewardedYandexAds.show()
            }
        })
    }

    OnLifecycleEvent { owner, event ->
        if(event == Lifecycle.Event.ON_DESTROY){
            rewardedYandexAds.destroy()
            interstitialYandexAds.destroy()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        timerButtonVisibility.start()
        viewModel.getUser { user = it }

        utilsDataStore.get(
            onSuccess = { utils = it }
        )
    })

    if(rewardAlertDialog){
        RewardAlertDialog(
            utils = utils,
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
                    phoneNumber = phoneNumber,
                    userEmail = user.email,
                    version = 2
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

    AnimatedVisibility(visible = !buttonVisibility){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = timerButtonVisibilityTickSecond.toString(),
                color = primaryText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W900,
                fontSize = 30.sp
            )

            Spacer(modifier = Modifier.height(90.dp))

            utils?.let {
                YandexAdsBanner(
                    size = AdSize.BANNER_240x400,
                    adUnitId = it.banner_yandex_ads_id
                )
            }
        }
    }

    AnimatedVisibility(visible = buttonVisibility) {
        Column {

            Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {

                SchoolBoard(
                    text = if(utils == null)
                        ""
                    else
                        "Балланс ${
                            userSumMoneyVersion2(
                                utils = utils!!,
                                countInterstitialAds = user.countInterstitialAds,
                                countInterstitialAdsClick = user.countInterstitialAdsClick,
                                countRewardedAds = user.countRewardedAds,
                                countRewardedAdsClick = user.countRewardedAdsClick
                            )
                        }",
                    width = (screenWidthDp / 1.1),
                    height = (screenHeightDp / 2.8)
                )
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

                AnimatedVisibility(visible = adsVisibility) {
                    MainButton(text = "Нажать") {
                        interstitialYandexAds.show()
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    MainButton(text = "Награда") {
                        rewardAlertDialog = true
                    }

                    if(user.userRole == UserRole.ADMIN){
                        MainButton(text = "Админ"){
                            navController.navigate(Screen.WithdrawalRequests.route)
                        }
                    }
                }

                utils?.let {
                    YandexAdsBanner(
                        adUnitId = it.banner_yandex_ads_id
                    )
                }
            }
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