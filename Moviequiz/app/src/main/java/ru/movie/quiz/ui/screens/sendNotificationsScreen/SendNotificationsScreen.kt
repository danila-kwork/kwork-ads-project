package ru.movie.quiz.ui.screens.sendNotificationsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import ru.movie.quiz.R
import ru.movie.quiz.data.fcm.NotificationDataBody
import ru.movie.quiz.data.fcm.PushNotificationBody
import ru.movie.quiz.data.fcm.fcmApi
import ru.movie.quiz.ui.screens.settingsScreen.BaseOutlinedTextField
import ru.movie.quiz.ui.theme.primaryText
import ru.movie.quiz.ui.theme.tintColor

@Composable
fun SendNotificationsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val scope = rememberCoroutineScope()
    val fcmApi = remember { fcmApi }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Image(
        bitmap = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(context.resources, R.drawable.fonstola),
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
        BaseOutlinedTextField(
            label = "Тема",
            value = title,
            onValueChange = { title = it }
        )

        BaseOutlinedTextField(
            label = "Сообщение",
            value = message,
            onValueChange = { message = it }
        )

        Button(
            modifier = Modifier.padding(10.dp),
            onClick = {
                scope.launch {
                    val response = fcmApi.pushFirebase(PushNotificationBody(NotificationDataBody(
                        title = title,
                        message = message
                    )))

                    if(response.isSuccessful){
                        navController.navigateUp()
                    }else {
                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = tintColor)
        ) {
            Text(text = "Отправить", color = primaryText)
        }
    }
}