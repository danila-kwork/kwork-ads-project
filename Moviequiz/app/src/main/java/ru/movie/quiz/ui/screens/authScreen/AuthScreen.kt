package ru.movie.quiz.ui.screens.authScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.movie.quiz.R
import ru.movie.quiz.data.auth.AuthDataStore
import ru.movie.quiz.ui.navigation.Screen
import ru.movie.quiz.ui.theme.primaryText
import ru.movie.quiz.ui.theme.secondaryBackground
import ru.movie.quiz.ui.theme.tintColor
import ru.movie.quiz.ui.view.BaseButton
import ru.movie.quiz.ui.view.BaseLottieAnimation
import ru.movie.quiz.ui.view.LottieAnimationType

@Composable
fun AuthScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    val authDataStore = remember(::AuthDataStore)

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
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(50.dp))

                BaseLottieAnimation(
                    type = LottieAnimationType.LOGIN,
                    modifier = Modifier
                        .size(330.dp)
                        .padding(5.dp)
                )

                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(5.dp).fillMaxWidth(),
                    fontWeight = FontWeight.W900,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
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
                    value = password,
                    onValueChange = { password = it },
                    shape = AbsoluteRoundedCornerShape(15.dp),
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

                BaseButton(
                    text = "Авторизироваться",
                    onClick = {
                        try {
                            authDataStore.signIn(email.trim(),password.trim(),{
                                navController.navigate(Screen.Main.route)
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

                BaseButton(
                    text = "Зарегистрироваться",
                    onClick = {
                        try {
                            authDataStore.registration(email.trim(),password.trim(),{
                                navController.navigate(Screen.Main.route)
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
}