package com.example.notes.ui.screens.withdrawalRequestsScreen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notes.LocalNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.notes.common.setClipboard
import com.example.notes.data.firebase.user.model.userSumMoney
import com.example.notes.data.firebase.utils.UtilsDataStore
import com.example.notes.data.firebase.utils.model.Utils
import com.example.notes.data.firebase.withdrawalRequest.model.WithdrawalRequest
import com.example.notes.navigation.Screen
import com.example.notes.ui.theme.primaryBackground
import com.example.notes.ui.theme.primaryText
import com.example.notes.ui.theme.tintColor

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WithdrawalRequestsScreen(
    viewModel: WithdrawalRequestsViewModel = viewModel(),
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val scope = rememberCoroutineScope()

    var message by remember { mutableStateOf("") }

    var withdrawalRequests by remember { mutableStateOf(listOf<WithdrawalRequest>()) }
    var deleteWithdrawalRequestId by remember { mutableStateOf("") }

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)

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

        utilsDataStore.get(onSuccess = {
            utils = it
        })
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

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = primaryBackground,
                title = {},
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            tint = primaryText
                        )
                    }
                }
            )
        }
    ) {
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
                            text = "Количество просмотренной рекламы : ${item.countAds}",
                            color = primaryText,
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countAds.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Сколько раз пользователь перешел на сайт : ${item.countAdsClick}",
                            color = primaryText,
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countAds.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Количество правельных ответов : ${item.countAnswers}",
                            color = primaryText,
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countAds.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Количество просмотреной рекламы, конпка Смотреть: ${item.countClickWatchAds}",
                            color = primaryText,
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, item.countAdsClick.toString())
                                    })
                                }
                        )

                        Text(
                            text = "Сумма для ввывода : ${userSumMoney(utils,item.countAds, item.countAnswers, item.countAdsClick, item.countClickWatchAds)}",
                            color = primaryText,
                            modifier = Modifier
                                .padding(5.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures(onLongPress = {
                                        setClipboard(context, userSumMoney(utils,item.countAds, item.countAnswers, item.countAdsClick, item.countClickWatchAds).toString())
                                    })
                                }
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