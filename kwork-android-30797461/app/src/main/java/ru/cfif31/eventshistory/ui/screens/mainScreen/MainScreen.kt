package ru.cfif31.eventshistory.ui.screens.mainScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.cfif31.eventshistory.ui.view.Calendar
import ru.cfif31.eventshistory.R
import ru.cfif31.eventshistory.data.firebase.realtimaDatabase.event.model.createEventLoading
import ru.cfif31.eventshistory.ui.view.DialogAds

@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    var count by rememberSaveable { mutableStateOf(0) }
    var dialogADSVisibility by rememberSaveable { mutableStateOf(false) }
    var event by remember { mutableStateOf(createEventLoading()) }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.getEventRandom(onSuccess = { event = it })
    })

    if (count == 50){
        event = createEventLoading()
        viewModel.getEventRandom(onSuccess = { event = it })
        count = 0
    }

    Box {

        Image(
            bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources,R.drawable.main_background),
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
//            Spacer(modifier = Modifier.padding(top = screenHeightDp.dp / 20))

            if(dialogADSVisibility){
                DialogAds(
                    onAdsShow = {
                        dialogADSVisibility = false
                        event = createEventLoading()
                        viewModel.getEventRandom(onSuccess = { event = it })
                    },
                    onDismissRequest = { dialogADSVisibility = false }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Calendar(
                    date = event.date,
                    modifier = Modifier
                        .size(
                            width = (screenWidthDp / 2).dp,
                            height = (screenHeightDp / 2).dp
                        )
                        .padding(
                            start = screenWidthDp.dp / 15
                        )
                )

                Text(
                    text = event.event,
                    color = Color.Red,
                    modifier = Modifier
                        .padding(5.dp)
                        .widthIn(max = (screenWidthDp / 3).dp),
                    fontWeight = FontWeight.W900
                )
            }
        }

        Column(
            modifier = Modifier.size(
                width = screenWidthDp.dp,
                height = screenHeightDp.dp
            ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom
        ) {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Image(
                    bitmap = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(context.resources,R.drawable.owl),
                        (screenWidthDp / 1.1).toInt(),
                        (screenHeightDp / 1.1).toInt(),
                        false
                    ).asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(
                        width = (screenWidthDp / 2).dp,
                        height = (screenHeightDp / 1.7).dp
                    )
                )

                Column {
                    Row {
                        Box {
                            Image(
                                bitmap = Bitmap.createScaledBitmap(
                                    BitmapFactory.decodeResource(context.resources,R.drawable.further),
                                    (screenWidthDp / 4.5).toInt(),
                                    screenHeightDp / 15,
                                    false
                                ).asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp,
                                        bottom = 5.dp
                                    )
                                    .height((screenHeightDp / 14.5).dp)
                                    .width((screenWidthDp / 4.5).dp)
                                    .clickable { count++ }
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp,
                                        bottom = 5.dp
                                    )
                                    .height((screenHeightDp / 14.5).dp)
                                    .width((screenWidthDp / 4.5).dp)
                                    .clickable { count++ }
                            ) {
                                Text(
                                    text = "Далее",
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        Box {
                            Image(
                                bitmap = Bitmap.createScaledBitmap(
                                    BitmapFactory.decodeResource(context.resources,R.drawable.count_click),
                                    (screenWidthDp / 5).toInt(),
                                    screenHeightDp / 15,
                                    false
                                ).asImageBitmap(),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp,
                                        bottom = 5.dp,
                                        end = 5.dp
                                    )
                                    .height((screenHeightDp / 14.5).dp)
                                    .width((screenWidthDp / 5).dp)
                            )

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .padding(
                                        top = 5.dp,
                                        bottom = 5.dp,
                                        end = 5.dp
                                    )
                                    .height((screenHeightDp / 14.5).dp)
                                    .width((screenWidthDp / 5).dp)
                            ) {
                                Text(
                                    text = "$count/50",
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }

                    Box {
                        Image(
                            bitmap = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(context.resources,R.drawable.skip),
                                (screenWidthDp / 2.4).toInt(),
                                (screenHeightDp / 13).toInt(),
                                false
                            ).asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(
                                    top = 5.dp,
                                    bottom = 5.dp,
                                    end = 5.dp
                                )
                                .height((screenHeightDp / 13).dp)
                                .width((screenWidthDp / 2.4).dp)
                                .clickable {
                                    dialogADSVisibility = true
                                }
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .padding(
                                    top = 5.dp,
                                    bottom = 5.dp,
                                    end = 5.dp
                                )
                                .height((screenHeightDp / 13).dp)
                                .width((screenWidthDp / 2.4).dp)
                                .clickable {
                                    dialogADSVisibility = true
                                }
                        ) {
                            Text(
                                text = "Пропустить",
                                color = Color.White,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(bottom = screenHeightDp.dp / 10))
                }
            }

            Spacer(modifier = Modifier.padding(bottom = screenHeightDp.dp / 15))
        }
    }
}