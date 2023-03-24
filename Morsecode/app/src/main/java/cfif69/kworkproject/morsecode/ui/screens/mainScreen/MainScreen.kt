package cfif69.kworkproject.morsecode.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.LocalTextStyle
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
import cfif69.kworkproject.morsecode.R
import cfif69.kworkproject.morsecode.data.morseCode.MorseCodeItem
import cfif69.kworkproject.morsecode.ui.view.SchoolBoard
import cfif69.kworkproject.morsecode.yandexAds.RewardedYandexAds

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = viewModel()
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val rewardedYandexAds = remember { RewardedYandexAds(context) }

    var userAnswer by rememberSaveable { mutableStateOf("") }
    var clickCount by rememberSaveable { mutableStateOf(0) }
    var yandexAdsCount by rememberSaveable { mutableStateOf(0) }
    var question by remember { mutableStateOf(MorseCodeItem()) }

    LaunchedEffect(key1 = Unit, block = {
        question = viewModel.getMorseCode()
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
                    text = "Какой символ зашифрован ${question.morse} ?",
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
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
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


                repeat(2){ index ->
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
                                        yandexAdsCount++
                                        userAnswer = question.symbol
                                    } else {
                                        if (userAnswer
                                                .lowercase()
                                                .trim() == question.symbol
                                                .lowercase()
                                                .trim()
                                        ) {
                                            userAnswer = ""
                                            question = viewModel.getMorseCode()

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
                                    }
                                }
                        ) {
                            Text(
                                text = if(index == 0) "Проверить" else "Подсказка",
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