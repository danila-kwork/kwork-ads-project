package ru.madtwoproject1.ui.screens.mainScreen.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.madtwoproject1.common.round
import ru.madtwoproject1.data.gift.GiftDataStore
import ru.madtwoproject1.ui.theme.primaryBackground
import ru.madtwoproject1.ui.theme.primaryText
import ru.madtwoproject1.ui.theme.tintColor
import kotlin.random.Random

@Composable
fun GiftAlertDialog(
    onDismissRequest: () -> Unit,
    onGift: (Double) -> Unit
) {
    val context = LocalContext.current

    val giftDataStore = remember { GiftDataStore(context) }
    var gift by remember { mutableStateOf(0.0) }
    var isGiftVisibility by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        isGiftVisibility = giftDataStore.isGiftVisibility()
    })

    AlertDialog(
        modifier = Modifier.height(200.dp),
        backgroundColor = primaryBackground,
        shape = AbsoluteRoundedCornerShape(20.dp),
        onDismissRequest = onDismissRequest,
        text = {
            Column {
                Text(
                    text = "Заходи каждый день в приложения и получай подарок",
                    color = primaryText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )

                AnimatedVisibility(visible = gift != 0.0) {
                    Text(
                        text = "Вы получили $gift рублей",
                        color = primaryText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W900
                    )
                }

                AnimatedVisibility(!isGiftVisibility && gift == 0.0){
                    Text(
                        text = "Сегодня вы уже получали подарок, возвращайся завтра",
                        color = primaryText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        buttons = {
            AnimatedVisibility(isGiftVisibility){
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = tintColor
                    ),
                    onClick = {
                        gift = round(Random.nextDouble(0.1,1.0), 3)
                        giftDataStore.saveDate()
                        isGiftVisibility = false
                        onGift(gift)
                    }
                ) {
                    Text(text = "Крутить", color = primaryText)
                }
            }
        }
    )
}