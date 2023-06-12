package com.lfybkf11701.watchads.ui.screens.authScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.lfybkf11701.watchads.R
import com.lfybkf11701.watchads.data.firebase.utils.UtilsDataStore
import com.lfybkf11701.watchads.data.firebase.utils.model.Utils
import com.lfybkf11701.watchads.ui.theme.primaryText
import com.lfybkf11701.watchads.ui.theme.secondaryBackground
import com.lfybkf11701.watchads.ui.theme.tintColor
import com.lfybkf11701.watchads.ui.view.YandexAdsBanner
import com.yandex.mobile.ads.banner.AdSize

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    var utils by remember { mutableStateOf<Utils?>(null) }
    val utilsDataStore = remember(::UtilsDataStore)

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
        modifier = Modifier.size(
            width = screenWidthDp.dp,
            height = screenHeightDp.dp
        ),
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
                modifier = Modifier.padding(5.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W900
            )

            OutlinedTextField(
                modifier = Modifier.padding(5.dp),
                value = email,
                onValueChange = { email = it },
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
                    Text(text = "Электронная почта", color = primaryText)
                }
            )

            OutlinedTextField(
                modifier = Modifier.padding(5.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
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
                    Text(text = "Пароль", color = primaryText)
                }
            )

            MainButton(
                text = "Авторизироваться",
                onClick = {
                    try {
                        viewModel.signIn(email.trim(),password.trim(),{
                            navController.navigate("Main")
                        },{
                            error = it
                        })
                    }catch(e: IllegalArgumentException){
                        error = "Заполните все поля"
                    }catch (e:Exception){
                        error = "Ошибка"
                    }
                }
            )

            MainButton(
                text = "Зарегистророваться",
                onClick = {
                    try {
                        viewModel.registration(email.trim(),password.trim(),{
                            navController.navigate("Main")
                        },{
                            error = it
                        })
                    }catch(e: IllegalArgumentException){
                        error = "Заполните все поля"
                    }catch (e:Exception){
                        error = "Ошибка"
                    }
                }
            )
        }
    }
}

@Composable
fun MainButton(
    text:String,
    onClick: () -> Unit
) {
    val gradient = Brush.horizontalGradient(listOf(
        tintColor,
        tintColor,
    ))

    Button(
        modifier = Modifier.padding(5.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.Transparent
        ),
        contentPadding = PaddingValues(),
        shape = AbsoluteRoundedCornerShape(15.dp),
        onClick = { onClick() })
    {
        Box(
            modifier = Modifier
                .background(gradient)
                .widthIn(min = 200.dp)
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .clip(AbsoluteRoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.White)
        }
    }
}