package ru.madtwoproject1.ui.screens.referralLinkScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.madtwoproject1.R
import ru.madtwoproject1.data.referral_link.ReferralLinkDataStore
import ru.madtwoproject1.data.user.UserDataStore
import ru.madtwoproject1.data.user.model.User
import ru.madtwoproject1.data.utils.UtilsDataStore
import ru.madtwoproject1.data.utils.model.Utils
import ru.madtwoproject1.ui.theme.primaryText
import ru.madtwoproject1.ui.theme.secondaryBackground
import ru.madtwoproject1.ui.theme.tintColor

@Composable
fun ReferralLinkScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val referralLinkDataStore = remember(::ReferralLinkDataStore)
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var myReferralLink by remember { mutableStateOf("") }
    var activeReferralLink by remember { mutableStateOf("") }
    var enterReferralLink by remember { mutableStateOf("") }
    var user by remember { mutableStateOf<User?>(null) }
    var utils by remember { mutableStateOf<Utils?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        referralLinkDataStore.create {
            myReferralLink = it
        }
        userDataStore.getActiveReferralLink {
            activeReferralLink = it.id
        }
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

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
        Text(
            text = "Пригласив друзей вы будете получать\n1 % от их выплат",
            color = primaryText,
            modifier = Modifier.padding(5.dp),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.W900
        )

        if(utils?.referral_link == false){
            Text(
                text = "Реферальные ссылки временно отключены",
                color = tintColor,
                modifier = Modifier.padding(5.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W900
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if(activeReferralLink.isNotEmpty() && activeReferralLink != "null"){
            Text(
                text = "Активная реферальная ссылка",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp),
                color = primaryText
            )

            OutlinedTextField(
                modifier = Modifier.padding(5.dp),
                value = activeReferralLink,
                onValueChange = {  },
                shape = AbsoluteRoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = primaryText,
                    disabledTextColor = tintColor,
                    backgroundColor = secondaryBackground,
                    cursorColor = tintColor,
                    focusedBorderColor = tintColor,
                    unfocusedBorderColor = secondaryBackground,
                    disabledBorderColor = secondaryBackground
                )
            )
        }else {
            Text(
                text = "Ведите реферальную ссылку друга",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp),
                color = primaryText
            )

            OutlinedTextField(
                modifier = Modifier.padding(5.dp),
                value = enterReferralLink,
                onValueChange = { enterReferralLink = it },
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
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {

                    if(enterReferralLink == user?.referralLink){
                        Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                        return@KeyboardActions
                    }

                    referralLinkDataStore.get(enterReferralLink.trim(),
                        onSuccess = {
                            navController.navigateUp()
                            Toast.makeText(context, "Успешно", Toast.LENGTH_SHORT).show()
                        },
                        onFailed = {
                            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show()
                        }
                    )
                })
            )
        }

        Text(
            text = "Ваша реферальная ссылка",
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp),
            color = primaryText
        )

        OutlinedTextField(
            modifier = Modifier.padding(5.dp),
            value = myReferralLink,
            onValueChange = {  },
            shape = AbsoluteRoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = primaryText,
                disabledTextColor = tintColor,
                backgroundColor = secondaryBackground,
                cursorColor = tintColor,
                focusedBorderColor = tintColor,
                unfocusedBorderColor = secondaryBackground,
                disabledBorderColor = secondaryBackground
            )
        )
    }
}