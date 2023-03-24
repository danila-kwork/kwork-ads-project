package com.MadJon.PINPNG.ui.screens.addAchievementScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.MadJon.PINPNG.R
import com.MadJon.PINPNG.data.achievement.AchievementDataStore
import com.MadJon.PINPNG.data.achievement.model.Achievement
import com.MadJon.PINPNG.data.achievement.model.AchievementType
import com.MadJon.PINPNG.ui.theme.primaryText
import com.MadJon.PINPNG.ui.theme.secondaryBackground
import com.MadJon.PINPNG.ui.theme.tintColor

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddAchievementScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var type by remember { mutableStateOf(AchievementType.QUESTIONS) }
    var count by remember { mutableStateOf("0") }
    var reward by remember { mutableStateOf("0") }
    val achievementDataStore = remember(::AchievementDataStore)

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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            AchievementType.values().forEach {
                Card(
                    modifier = Modifier.padding(5.dp),
                    backgroundColor = secondaryBackground,
                    shape = AbsoluteRoundedCornerShape(10.dp),
                    onClick = { type = it },
                    border = if(type == it)
                        BorderStroke(3.dp, tintColor)
                    else
                        null
                ) {
                    Text(
                        text = it.text,
                        color = primaryText,
                        modifier = Modifier.padding(15.dp)
                    )
                }
            }
        }

        OutlinedTextField(
            modifier = Modifier.padding(5.dp),
            value = count,
            onValueChange = { count = it },
            shape = AbsoluteRoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = primaryText,
                disabledTextColor = tintColor,
                backgroundColor = secondaryBackground,
                cursorColor = tintColor,
                focusedBorderColor = tintColor,
                unfocusedBorderColor = secondaryBackground,
                disabledBorderColor = secondaryBackground
            ),
            label = {
                Text(
                    text = when(type){
                        AchievementType.QUESTIONS -> "Колиство правильных ответов"
                        AchievementType.INTERESTING_FACT -> "Количество просмотренных фактов"
                    },
                    color = tintColor
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        OutlinedTextField(
            modifier = Modifier.padding(5.dp),
            value = reward,
            onValueChange = { reward = it },
            shape = AbsoluteRoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = primaryText,
                disabledTextColor = tintColor,
                backgroundColor = secondaryBackground,
                cursorColor = tintColor,
                focusedBorderColor = tintColor,
                unfocusedBorderColor = secondaryBackground,
                disabledBorderColor = secondaryBackground
            ),
            label = {
                Text(text = "Награда в рублях", color = tintColor)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )

        Button(
            modifier = Modifier.padding(10.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = tintColor),
            shape = AbsoluteRoundedCornerShape(10.dp),
            onClick = {
                achievementDataStore.create(
                    achievement = Achievement(
                        type = type,
                        count = count.toInt(),
                        reward = reward.toDouble()
                    )
                ){
                    navController.navigateUp()
                }
            }
        ) {
            Text(
                text = "Добавить",
                color = primaryText
            )
        }
    }
}