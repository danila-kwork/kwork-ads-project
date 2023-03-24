package ru.lfyblf_cfif31.moneyproject_kwork.ui.screens.mainScreen.view

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.lfyblf_cfif31.moneyproject_kwork.data.firebase.user.model.userSumMoney
import ru.lfyblf_cfif31.moneyproject_kwork.ui.theme.primaryBackground
import ru.lfyblf_cfif31.moneyproject_kwork.ui.theme.primaryText
import ru.lfyblf_cfif31.moneyproject_kwork.ui.theme.secondaryBackground
import ru.lfyblf_cfif31.moneyproject_kwork.ui.theme.tintColor

@Composable
fun RewardAlertDialog(
    countInterstitialAds:Int,
    countInterstitialAdsClick:Int,
    countRewardedAds:Int,
    countRewardedAdsClick:Int,
    onDismissRequest:() -> Unit,
    onSendWithdrawalRequest: (phoneNumber: String) -> Unit
) {
    val context = LocalContext.current

    var phoneNumber by remember { mutableStateOf("") }

    AlertDialog(
        backgroundColor = primaryBackground,
        shape = AbsoluteRoundedCornerShape(20.dp),
        onDismissRequest = onDismissRequest,
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Вы можете отправить заявку на вывод средств." +
                        " Деньги предут вам в течение трех рабочих дней." +
                        "\nВывод на киви кошелёк." +
                        "\nМинимальная сумму для вывода 50 рублей.",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(5.dp),
                    color = primaryText
                )
                
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    modifier = Modifier.padding(5.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
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
                        Text(text = "Номер телефона", color = primaryText)
                    }
                )
            }
        },
        buttons = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = tintColor
                ),
                onClick = {
                    verifySendWithdrawalRequest(
                        context,
                        countInterstitialAds = countInterstitialAds,
                        countInterstitialAdsClick = countInterstitialAdsClick,
                        countRewardedAds = countRewardedAds,
                        countRewardedAdsClick = countRewardedAdsClick,
                        phoneNumber.trim(),
                        onSendWithdrawalRequest
                    )
                }
            ) {
                Text(text = "Отправить", color = primaryText)
            }
        }
    )
}

private fun verifySendWithdrawalRequest(
    context: Context,
    countInterstitialAds:Int,
    countInterstitialAdsClick:Int,
    countRewardedAds:Int,
    countRewardedAdsClick:Int,
    phoneNumber: String,
    onSendWithdrawalRequest:(cardNumber: String) -> Unit
){
    if(userSumMoney(
            countInterstitialAds = countInterstitialAds,
            countInterstitialAdsClick = countInterstitialAdsClick,
            countRewardedAds = countRewardedAds,
            countRewardedAdsClick = countRewardedAdsClick
    ) < 40){
        Toast.makeText(context, "Минимальная сумма для вывода 40 рублей", Toast.LENGTH_SHORT).show()
    }else if(phoneNumber.isEmpty()){
        Toast.makeText(context, "Укажите номер телефона", Toast.LENGTH_SHORT).show()
    }else {
        onSendWithdrawalRequest(phoneNumber)
    }
}