package ru.cthuttdbx05.moneyproject_kwork.ui.screens.authScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yandex.mobile.ads.banner.AdSize
import ru.cthuttdbx05.moneyproject_kwork.LocalNavController
import ru.cthuttdbx05.moneyproject_kwork.navigation.Screen
import ru.cthuttdbx05.moneyproject_kwork.ui.theme.primaryText
import ru.cthuttdbx05.moneyproject_kwork.ui.theme.secondaryBackground
import ru.cthuttdbx05.moneyproject_kwork.ui.theme.tintColor
import ru.cthuttdbx05.moneyproject_kwork.ui.view.YandexAdsBanner
import ru.cthuttdbx05.moneyproject_kwork.R
import ru.cthuttdbx05.moneyproject_kwork.data.firebase.utils.UtilsDataStore
import ru.cthuttdbx05.moneyproject_kwork.data.firebase.utils.model.Utils

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val navController = LocalNavController.current

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        utilsDataStore.get(onSuccess = {
            utils = it
        })
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            utils?.let {
                YandexAdsBanner(
                    size = AdSize.BANNER_240x400,
                    adUnitId = it.banner_yandex_ads_id
                )
            }
        }

        item {
            Text(
                text = error,
                color = Color.Red,
                modifier = Modifier.padding(5.dp)
            )

            OutlinedTextField(
                modifier = Modifier.padding(5.dp),
                value = email,
                onValueChange = { email = it },
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
                    Text(text = "Электронная почта", color = primaryText)
                }
            )

            OutlinedTextField(
                modifier = Modifier.padding(5.dp),
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
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
                    Text(text = "Парроль", color = primaryText)
                }
            )

            Button(
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    viewModel.signIn(email.trim(),password.trim(),{
                        navController.navigate(Screen.Main.route)
                    },{
                        error = it
                    })
                }
            ) {
                Text(text = "Авторизироваться", color = primaryText)
            }

            Button(
                modifier = Modifier.padding(5.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    viewModel.registration(email,password,{
                        navController.navigate(Screen.Main.route)
                    },{
                        error = it
                    })
                }
            ) {
                Text(text = "Зарегестророваться", color = primaryText)
            }
        }
    }
}