package ru.cthuttdbx17.moneyproject_kwork.ui.screens.withdrawalRequestsScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.cthuttdbx17.moneyproject_kwork.common.setClipboard
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.user.model.userSumMoneyVersion1
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.user.model.userSumMoneyVersion2
import ru.cthuttdbx17.moneyproject_kwork.data.firebase.withdrawalRequest.model.WithdrawalRequest
import ru.cthuttdbx17.moneyproject_kwork.ui.theme.primaryBackground
import ru.cthuttdbx17.moneyproject_kwork.ui.theme.primaryText
import ru.cthuttdbx17.moneyproject_kwork.ui.theme.tintColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WithdrawalRequestsScreen(
    viewModel: WithdrawalRequestsViewModel = viewModel(),
) {
    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var message by remember { mutableStateOf("") }

    var withdrawalRequests by remember { mutableStateOf(listOf<WithdrawalRequest>()) }
    var deleteWithdrawalRequestId by remember { mutableStateOf("") }

    var isRefreshing by remember { mutableStateOf(false) }

    val pullRefreshState = rememberPullRefreshState(isRefreshing, {
        isRefreshing = true

        viewModel.getWithdrawalRequests {
            scope.launch {
                withdrawalRequests = it

                if(it.isEmpty())
                    message = "Пусто"

                delay(500L)

                isRefreshing = false
            }
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getWithdrawalRequests {
            withdrawalRequests = it

            if(it.isEmpty())
                message = "Пусто"
        }
    })

    if(deleteWithdrawalRequestId.isNotEmpty()){
        DeleteWithdrawalRequestAlertDialog(
            onDismissRequest = {
                deleteWithdrawalRequestId = ""
            },
            confirm = {
                viewModel.deleteWithdrawalRequest(
                    id = deleteWithdrawalRequestId,
                    onSuccess = {
                        deleteWithdrawalRequestId = ""
                    },
                    onError = {
                        Toast.makeText(context, "Ошибка: $it", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        color = primaryBackground
    ) {
        Box(Modifier.pullRefresh(pullRefreshState)) {

            LazyColumn(modifier = Modifier.fillMaxSize()) {

                item {
                    AnimatedVisibility(visible = message.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = message,
                                color = Color.Red,
                                fontWeight = FontWeight.W900,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                items(withdrawalRequests){ item ->

                    val sum = if(item.version == 2)
                        userSumMoneyVersion2(
                            countInterstitialAds = item.countInterstitialAds,
                            countInterstitialAdsClick = item.countInterstitialAdsClick,
                            countRewardedAds = item.countRewardedAds,
                            countRewardedAdsClick = item.countRewardedAdsClick
                        )
                    else
                        userSumMoneyVersion1(
                            countInterstitialAds = item.countInterstitialAds,
                            countInterstitialAdsClick = item.countInterstitialAdsClick,
                            countRewardedAds = item.countRewardedAds,
                            countRewardedAdsClick = item.countRewardedAdsClick
                        )

                    Text(
                        text = "Индификатор пользователя : ${item.userId}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, item.userId)
                                })
                            }
                    )

                    Text(
                        text = "Электронная почта : ${item.userEmail}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, item.userEmail)
                                })
                            }
                    )

                    Text(
                        text = "Номер телефона : ${item.phoneNumber}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, item.phoneNumber)
                                })
                            }
                    )

                    Text(
                        text = "Полноэкраная : ${item.countInterstitialAds}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, (item.countInterstitialAds.toString()))
                                })
                            }
                    )

                    Text(
                        text = "Полноэкраная переход 10 сек : ${item.countInterstitialAdsClick}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, item.countInterstitialAdsClick.toString())
                                })
                            }
                    )

                    Text(
                        text = "Вознаграждением : ${item.countRewardedAds}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, item.countRewardedAds.toString())
                                })
                            }
                    )

                    Text(
                        text = "Вознаграждением переход на 10 сек: ${item.countRewardedAdsClick}",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, item.countRewardedAdsClick.toString())
                                })
                            }
                    )

                    Text(
                        text = "Сумма для ввывода : $sum",
                        color = primaryText,
                        modifier = Modifier
                            .padding(5.dp)
                            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    setClipboard(context, sum.toString())
                                })
                            }
                    )

                    Text(
                        text = if(item.version == 2) "Версия 2" else "Версия 1",
                        color = tintColor,
                        modifier = Modifier.padding(5.dp).fillMaxWidth(),
                        textAlign = TextAlign.End
                    )

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        onClick = { deleteWithdrawalRequestId = item.id },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = tintColor
                        )
                    ) {
                        Text(text = "Удалить", color = primaryText)
                    }

                    Divider(color = tintColor)
                }
            }

            PullRefreshIndicator(isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}

@Composable
private fun DeleteWithdrawalRequestAlertDialog(
    onDismissRequest: () -> Unit,
    confirm: () -> Unit
) {
    AlertDialog(
        backgroundColor = primaryBackground,
        shape = AbsoluteRoundedCornerShape(15.dp),
        onDismissRequest = onDismissRequest,
        buttons = {
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                onClick = confirm
            ) {
                Text(text = "Подтвердить", color = Color.Red)
            }
        }
    )
}