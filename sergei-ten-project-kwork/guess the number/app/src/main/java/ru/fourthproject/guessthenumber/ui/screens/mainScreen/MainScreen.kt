package ru.fourthproject.guessthenumber.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import ru.fourthproject.guessthenumber.ui.view.SchoolBoard
import ru.fourthproject.guessthenumber.R
import ru.fourthproject.guessthenumber.data.firebase.user.model.User
import ru.fourthproject.guessthenumber.data.firebase.user.model.UserRole
import ru.fourthproject.guessthenumber.navigation.Screen
import ru.fourthproject.guessthenumber.yandexAds.InterstitialYandexAds
import ru.fourthproject.guessthenumber.yandexAds.RewardedYandexAds

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val interstitialYandexAds = remember { InterstitialYandexAds(context) }
    val rewardedYandexAds = remember { RewardedYandexAds(context) }

    var userAnswer by rememberSaveable { mutableStateOf("") }
    var yandexAdsCount by rememberSaveable { mutableStateOf(0) }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var user by remember { mutableStateOf<User?>(null) }

    val auth = remember { FirebaseAuth.getInstance() }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getNumberRandom().apply {
            question = this.question
            answer = this.answer
        }

        viewModel.getUser { user = it }
    })

    if(yandexAdsCount >= 3){
        rewardedYandexAds.show()
        yandexAdsCount = 0
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
                    text = question,
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


                MainButton(text = "Проверить") {
                    if (userAnswer.lowercase() == answer.lowercase()) {
                        userAnswer = ""
                        viewModel
                            .getNumberRandom()
                            .apply {
                                question =
                                    "Правильно, давай ещё раз\n${this.question}"
                                answer = this.answer
                            }
                    } else {

                        val x = answer.toIntOrNull()
                        val y = userAnswer.toIntOrNull()

                        if (x != null && y != null) {
                            yandexAdsCount++

                            val text =
                                if (x > y)
                                    "Число больше $userAnswer"
                                else
                                    "Число меньше $userAnswer"

                            question = text
                        }
                    }
                }

                MainButton(text = "Подсказка") {
                    yandexAdsCount++
                    userAnswer = answer
                }

                MainButton(text = "Нажми") {
                    interstitialYandexAds.show()
                }

                if(auth.currentUser == null){
                    MainButton(text = "Войти") {
                        navController.navigate(Screen.AuthScreen.route)
                    }
                }

                if(user?.userRole == UserRole.ADMIN){
                    MainButton(text = "Админ") {
                        navController.navigate(Screen.SettingsScreen.route)
                    }
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