package ru.lfybkf19.moneyproject_kwork.ui.screens.mainScreen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.lfybkf19.moneyproject_kwork.LocalNavController
import ru.lfybkf19.moneyproject_kwork.R
import ru.lfybkf19.moneyproject_kwork.data.firebase.user.model.UserRole
import ru.lfybkf19.moneyproject_kwork.data.firebase.user.model.createUserLoading
import ru.lfybkf19.moneyproject_kwork.data.firebase.user.model.userSumMoney
import ru.lfybkf19.moneyproject_kwork.data.firebase.withdrawalRequest.model.WithdrawalRequest
import ru.lfybkf19.moneyproject_kwork.navigation.Screen
import ru.lfybkf19.moneyproject_kwork.ui.screens.mainScreen.view.RewardAlertDialog
import ru.lfybkf19.moneyproject_kwork.ui.view.OnLifecycleEvent
import ru.lfybkf19.moneyproject_kwork.ui.view.SchoolBoard
import ru.lfybkf19.moneyproject_kwork.ui.view.YandexAdsBanner
import ru.lfybkf19.moneyproject_kwork.yandexAds.InterstitialYandexAds
import ru.lfybkf19.moneyproject_kwork.yandexAds.RewardedYandexAds
import java.time.Duration

@SuppressLint("NewApi")
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val context = LocalContext.current

    val navController = LocalNavController.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var user by remember { mutableStateOf(createUserLoading()) }

    var rewardAlertDialog by remember { mutableStateOf(false) }

    val rewardedYandexAds = remember {
        RewardedYandexAds(context, onDismissed = { adClickedDate, returnedToDate, rewarded ->
            viewModel.updateCountRewardedAds(user.countRewardedAds + 1)
        })
    }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            viewModel.updateCountInterstitialAds(user.countInterstitialAds + 1)
        })
    }

    OnLifecycleEvent { owner, event ->
        if(event == Lifecycle.Event.ON_DESTROY){
            rewardedYandexAds.destroy()
            interstitialYandexAds.destroy()
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getUser { user = it }
    })

    if(rewardAlertDialog){
        RewardAlertDialog(
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
                    userEmail = user.email
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

    Column {

        Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {

            SchoolBoard(
                text = "Балланс ${
                    userSumMoney(
                    countInterstitialAds = user.countInterstitialAds,
                    countInterstitialAdsClick = user.countInterstitialAdsClick,
                    countRewardedAds = user.countRewardedAds,
                    countRewardedAdsClick = user.countRewardedAdsClick
                )
                }",
                width = (screenWidthDp / 2.0),
                height = (screenHeightDp / 2.5)
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

            Row {
                MainButton(text = "Смотреть видео") {
                    rewardedYandexAds.show()
                }

                MainButton(text = "Смотреть") {
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

            YandexAdsBanner()
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