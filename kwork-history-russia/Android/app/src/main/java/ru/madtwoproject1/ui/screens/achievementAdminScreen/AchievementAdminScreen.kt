package ru.madtwoproject1.ui.screens.achievementAdminScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.madtwoproject1.R
import ru.madtwoproject1.data.achievement.AchievementDataStore
import ru.madtwoproject1.data.achievement.model.Achievement
import ru.madtwoproject1.data.achievement.model.AchievementType
import ru.madtwoproject1.ui.theme.primaryText
import ru.madtwoproject1.ui.theme.secondaryBackground
import ru.madtwoproject1.ui.theme.tintColor

@Composable
fun AchievementAdminScreen(

) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val achievementDataStore = remember(::AchievementDataStore)
    var achievementList by remember { mutableStateOf(listOf<Achievement>()) }

    LaunchedEffect(key1 = Unit, block = {
        achievementDataStore.getList {
            achievementList = it
        }
    })

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

    LazyColumn {
        items(achievementList){ item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 15.dp,
                        vertical = 5.dp
                    ),
                shape = AbsoluteRoundedCornerShape(15.dp),
                backgroundColor = secondaryBackground
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        when(item.type){
                            AchievementType.QUESTIONS -> {
                                Text(
                                    text = "Ответьте на вопросы",
                                    color = primaryText,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                            AchievementType.INTERESTING_FACT -> {
                                Text(
                                    text = "Посмотрите\nинтересные факты",
                                    color = primaryText,
                                    modifier = Modifier.padding(5.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Text(
                            text = "${item.count}",
                            color = primaryText,
                            modifier = Modifier.padding(5.dp)
                        )
                    }

                    Text(
                        text = "Награда ${item.reward} рублей",
                        modifier = Modifier
                            .padding(5.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = primaryText
                    )

                    Button(
                        modifier = Modifier.padding(5.dp).align(Alignment.CenterHorizontally),
                        shape = AbsoluteRoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = tintColor
                        ),
                        onClick = {
                            achievementDataStore.deleteById(
                                id = item.id
                            ){
                                achievementDataStore.getList {
                                    achievementList = it
                                }
                            }
                        }
                    ) {
                        Text(text = "Удалить", color = primaryText)
                    }
                }
            }
        }
    }
}