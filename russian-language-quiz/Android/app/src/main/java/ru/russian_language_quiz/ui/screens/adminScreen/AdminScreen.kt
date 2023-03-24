package ru.russian_language_quiz.ui.screens.adminScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.russian_language_quiz.data.withdrawalRequest.model.WithdrawalRequestStatus
import ru.russian_language_quiz.ui.navigation.Screen
import ru.russian_language_quiz.ui.theme.primaryText
import ru.russian_language_quiz.ui.theme.tintColor
import ru.russian_language_quiz.R
import ru.russian_language_quiz.data.utils.UtilsDataStore
import ru.russian_language_quiz.data.utils.model.Utils
import ru.russian_language_quiz.ui.screens.adminScreen.view.AddInfoAlertDialog

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AdminScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    var addInfoAlertDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        utilsDataStore.get({utils = it})
    })

    if(addInfoAlertDialog){
        AddInfoAlertDialog(
            info = utils?.info ?: "",
            onDismissRequest = { addInfoAlertDialog = false },
            onUpdateInfo = {
                utilsDataStore.updateInfo(it){
                    addInfoAlertDialog = false
                    utilsDataStore.get({utils = it})
                }
            }
        )
    }

    Surface(
        color = Color(0xFF4479af)
    ) {
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
//            AnimatedVisibility(visible = utils?.referral_link != null) {
//                var referralLink by remember { mutableStateOf(utils!!.referral_link) }
//
//                Card(
//                    modifier = Modifier
//                        .padding(horizontal = 15.dp, vertical = 5.dp)
//                        .fillMaxWidth(),
//                    backgroundColor = if(referralLink)
//                        tintColor
//                    else
//                        primaryBackground,
//                    shape = AbsoluteRoundedCornerShape(15.dp),
//                    onClick = {
//                        utilsDataStore.updateReferralLink(!referralLink) {
//                            referralLink = !referralLink
//                        }
//                    }
//                ) {
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = "Реферальные ссылки",
//                            fontWeight = FontWeight.W900,
//                            modifier = Modifier.padding(5.dp),
//                            color = primaryText
//                        )
//
//                        Switch(
//                            checked = referralLink,
//                            modifier = Modifier.padding(5.dp),
//                            onCheckedChange = {
//                                utilsDataStore.updateReferralLink(it) {
//                                    referralLink = it
//                                }
//                            }
//                        )
//                    }
//                }
//            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    navController.navigate(
                        "${Screen.WithdrawalRequests.route}/${WithdrawalRequestStatus.PAID}"
                    )
                }
            ) {
                Text(
                    text = "Заявки статус '${WithdrawalRequestStatus.PAID.text}'",
                    color = primaryText
                )
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    navController.navigate(
                        "${Screen.WithdrawalRequests.route}/${WithdrawalRequestStatus.WAITING}"
                    )
                }
            ) {
                Text(
                    text = "Заявки статус '${WithdrawalRequestStatus.WAITING.text}'",
                    color = primaryText
                )
            }

//            Button(
//                modifier = Modifier
//                    .padding(horizontal = 15.dp, vertical = 5.dp)
//                    .fillMaxWidth()
//                    .height(60.dp),
//                shape = AbsoluteRoundedCornerShape(15.dp),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = tintColor
//                ),
//                onClick = {
//                    navController.navigate(Screen.AchievementAdmin.route)
//                }
//            ) {
//                Text(text = "Достижения", color = primaryText)
//            }
//
//            Button(
//                modifier = Modifier
//                    .padding(horizontal = 15.dp, vertical = 5.dp)
//                    .fillMaxWidth()
//                    .height(60.dp),
//                shape = AbsoluteRoundedCornerShape(15.dp),
//                colors = ButtonDefaults.buttonColors(
//                    backgroundColor = tintColor
//                ),
//                onClick = {
//                    navController.navigate(Screen.AddAchievement.route)
//                }
//            ) {
//                Text(text = "Добавить достижение", color = primaryText)
//            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    addInfoAlertDialog = true
                }
            ) {
                Text(text = "Изменить информацию", color = primaryText)
            }

            Button(
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    navController.navigate(Screen.Settings.route)
                }
            ) {
                Text(text = "Настройки", color = primaryText)
            }
        }
    }
}