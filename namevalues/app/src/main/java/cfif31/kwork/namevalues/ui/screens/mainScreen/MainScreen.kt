package cfif31.kwork.namevalues.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import cfif31.kwork.namevalues.R
import cfif31.kwork.namevalues.data.firebase.name.model.createNameLoading
import cfif31.kwork.namevalues.ui.view.StationerySticker
import cfif31.kwork.namevalues.yandexAds.RewardedYandexAds

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val rewardedYandexAds = remember { RewardedYandexAds(context) }

    var userAnswer by rememberSaveable { mutableStateOf("") }
    var clickCount by rememberSaveable { mutableStateOf(0) }
    var yandexAdsCount by rememberSaveable { mutableStateOf(0) }
    var name by remember { mutableStateOf(createNameLoading()) }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getNameRandom(onSuccess = { name = it })
    })

//    if (clickCount >= 3){
//        rewardedYandexAds.show()
//        clickCount = 0
//    }

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
                StationerySticker(
                    text = name.value,
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
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height((screenHeightDp / 15).dp))

                Text(
                    text = "Ответ:",
                    color = Color.White,
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


                repeat(2){ index ->
                    Box {
                        Image(
                            bitmap = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(context.resources, R.drawable.main_button),
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
                                    if (clickCount < 3){
                                        clickCount++
                                    }

                                    if (index == 1) {
                                        yandexAdsCount++
                                        userAnswer = name.name
                                    } else {
                                        if (userAnswer.lowercase() == name.name.lowercase()) {
                                            userAnswer = ""
                                            name = createNameLoading()
                                            viewModel.getNameRandom(onSuccess = {
                                                name = it
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