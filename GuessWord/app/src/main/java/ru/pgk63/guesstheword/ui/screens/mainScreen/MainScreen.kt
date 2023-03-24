package ru.pgk63.guesstheword.ui.screens.mainScreen

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.pgk63.guesstheword.LocalNavController
import ru.pgk63.guesstheword.R
import ru.pgk63.guesstheword.data.firebase.user.model.UserRole
import ru.pgk63.guesstheword.data.firebase.user.model.createUserLoading
import ru.pgk63.guesstheword.data.firebase.user.model.userSumAds
import ru.pgk63.guesstheword.data.firebase.withdrawalRequest.model.WithdrawalRequest
import ru.pgk63.guesstheword.data.firebase.words.model.createWordLoading
import ru.pgk63.guesstheword.navigation.Screen
import ru.pgk63.guesstheword.ui.screens.mainScreen.view.RewardAlertDialog
import ru.pgk63.guesstheword.ui.view.SchoolBoard
import ru.pgk63.guesstheword.yandexAds.RewardedYandexAds


private const val technicalSupportUrl = "https://t.me/+pcjkh1GtITplNGIy"

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val context = LocalContext.current

    val navController = LocalNavController.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var user by remember { mutableStateOf(createUserLoading()) }

    var answerAds by rememberSaveable { mutableStateOf(false) }
    var userAnswer by rememberSaveable { mutableStateOf("") }
    var clickCount by rememberSaveable { mutableStateOf(0) }
    var yandexAdsCount by rememberSaveable { mutableStateOf(0) }
    var word by remember { mutableStateOf(createWordLoading()) }

    var rewardAlertDialog by remember { mutableStateOf(false) }

    val rewardedYandexAds = remember { RewardedYandexAds(context){ viewModel.updateCountAds(user.countAds + 1) } }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getRandom(onSuccess = { word = it })
        viewModel.getUser { user = it }
    })

    if(yandexAdsCount >= 3){
        rewardedYandexAds.show()
        yandexAdsCount = 0
    }

    if(rewardAlertDialog){
        RewardAlertDialog(
            countAds = user.countAds,
            countAnswers = user.countAnswers,
            onDismissRequest = {
                rewardAlertDialog = false
            },
            onSendWithdrawalRequest = { phoneNumber ->
                val withdrawalRequest = WithdrawalRequest(
                    countAds = user.countAds,
                    countAnswers = user.countAnswers,
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
        Row {
            Column {
                SchoolBoard(
                    text = "Балланс ${userSumAds(user.countAds, user.countAnswers)} ₽\n\nКакая буква пропущена ${word.word} ?",
                    width = (screenWidthDp / 2.0),
                    height = (screenHeightDp / 2.5)
                )

                Image(
                    bitmap = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(context.resources,R.drawable.owl),
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
                            BitmapFactory.decodeResource(context.resources,R.drawable.text_field_background),
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


                repeat(if(user.userRole == UserRole.ADMIN) 5 else 4){ index ->
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
                                .clickable {
                                    if (clickCount < 3) {
                                        clickCount++
                                    }

                                    if (index == 1) {
                                        answerAds = true
                                        yandexAdsCount++
                                        userAnswer = word.symbol
                                    } else if (index == 0) {
                                        if (userAnswer
                                                .lowercase()
                                                .trim() == word.symbol
                                                .lowercase()
                                                .trim()
                                        ) {
                                            if (!answerAds) {
                                                viewModel.updateCountAnswers(user.countAnswers + 1)
                                            }

                                            userAnswer = ""
                                            answerAds = false
                                            word = createWordLoading()
                                            viewModel.getRandom(onSuccess = {
                                                word = it
                                            })
                                            Toast
                                                .makeText(context,
                                                    "Верно !",
                                                    Toast.LENGTH_SHORT)
                                                .show()
                                        } else {
                                            Toast
                                                .makeText(context,
                                                    "Не верно",
                                                    Toast.LENGTH_SHORT)
                                                .show()
                                        }
                                    } else if (index == 2) {
                                        rewardAlertDialog = true
                                    } else if (index == 4) {
                                        navController.navigate(Screen.WithdrawalRequests.route)
                                    } else if (index == 3) {
                                        val browserIntent = Intent(Intent.ACTION_VIEW,
                                            Uri.parse(technicalSupportUrl))
                                        context.startActivity(browserIntent)
                                    }
                                }
                        ) {
                            Text(
                                text = when (index) {
                                    0 -> "Проверить"
                                    1 -> "Подсказка"
                                    2 -> "Награда"
                                    3 -> "Поддержка"
                                    else -> "Админ"
                                },
                                color = Color.Yellow,
                                fontSize = 20.sp,
                                fontStyle = FontStyle.Italic
                            )
                        }

                    }
                }
            }
        }
    }
}