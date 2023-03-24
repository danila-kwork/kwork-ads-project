package ru.thirdproject.population.ui.screens.mainScreen

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
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import ru.thirdproject.population.LocalNavController
import ru.thirdproject.population.R
import ru.thirdproject.population.data.firebase.population.model.createPopulationQuestionLoading
import ru.thirdproject.population.data.firebase.user.model.UserRole
import ru.thirdproject.population.data.firebase.user.model.createUserLoading
import ru.thirdproject.population.data.firebase.user.model.userSumMoneyVersion2
import ru.thirdproject.population.data.firebase.utils.UtilsDataStore
import ru.thirdproject.population.data.firebase.utils.model.Utils
import ru.thirdproject.population.data.firebase.withdrawalRequest.model.WithdrawalRequest
import ru.thirdproject.population.navigation.Screen
import ru.thirdproject.population.ui.screens.mainScreen.view.RewardAlertDialog
import ru.thirdproject.population.ui.theme.primaryText
import ru.thirdproject.population.ui.view.OnLifecycleEvent
import ru.thirdproject.population.ui.view.SchoolBoard
import ru.thirdproject.population.ui.view.YandexAdsBanner
import ru.thirdproject.population.ui.view.addCountBannerWatch
import ru.thirdproject.population.yandexAds.InterstitialYandexAds
import ru.thirdproject.population.yandexAds.RewardedYandexAds
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
    var userAnswer by rememberSaveable { mutableStateOf("") }
    var question by remember { mutableStateOf(createPopulationQuestionLoading()) }

    var rewardAlertDialog by remember { mutableStateOf(false) }
    val buttonVisibility by remember { mutableStateOf(true) }
    var adsVisibility by remember { mutableStateOf(true) }
    val timerButtonVisibilityTickSecond by remember { mutableStateOf(0L) }

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)

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
        viewModel.getUser { user = it }

        utilsDataStore.get(onSuccess = { utils = it })
        viewModel.getPopulationRandom(onSuccess = {question = it})
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

    AnimatedVisibility(visible = buttonVisibility) {

        Column {
            Row {
                Column {
                    SchoolBoard(
                        text = "${question.question}\n" +
                                "\nБаланс\n${
                                    userSumMoneyVersion2(
                                        utils = utils,
                                        countInterstitialAds = user.countInterstitialAds,
                                        countInterstitialAdsClick = user.countInterstitialAdsClick,
                                        countRewardedAds = user.countRewardedAds,
                                        countRewardedAdsClick = user.countRewardedAdsClick,
                                        countBannerAds = user.countBannerAds,
                                        countBannerAdsClick = user.countBannerAdsClick
                                    )
                                }",
                        width = (screenWidthDp / 2.0),
                        height = (screenHeightDp / 2.5)
                    )

                    Image(
                        bitmap = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(context.resources, R.drawable.owl),
                            screenWidthDp,
                            (screenHeightDp / 1),
                            false
                        ).asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(
                            width = (screenWidthDp / 1.9).dp,
                            height = (screenHeightDp / 1).dp
                        )
                    )
                }

                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center
                ) {

                    Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

                    Text(
                        text = "Ответ:",
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontSize = 40.sp
                    )

                    Box {
                        Image(
                            bitmap = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(
                                    context.resources,
                                    R.drawable.text_field_background
                                ),
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

                        ) {
                            TextField(
                                value = userAnswer,
                                onValueChange = { userAnswer = it },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = Color.White,
                                    backgroundColor = Color.Transparent,
                                    unfocusedLabelColor = Color.Transparent,
                                    focusedLabelColor = Color.Transparent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))


                    MainButton(text = "Проверить") {

                        interstitialYandexAds.show()

                        if (userAnswer.lowercase() == question.answer.lowercase()) {
                            userAnswer = ""
                            question = createPopulationQuestionLoading()
                            viewModel.getPopulationRandom(onSuccess = {
                                question = it
                            })
                            Toast
                                .makeText(
                                    context,
                                    "Верно !",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Не верно",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }


                    MainButton(text = "Подсказка") {
                        userAnswer = question.answer
                        rewardedYandexAds.show()
                    }

                    MainButton(text = "Награда") {
                        rewardAlertDialog = true
                    }

                    if(user.userRole == UserRole.ADMIN){
                        MainButton(text = "Админ") {
                            navController.navigate(Screen.WithdrawalRequests.route)
                        }
                    }
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