package ru.arseniy_two_project.moneyproject_kwork.ui.screens.mainScreen.view

import android.os.CountDownTimer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.arseniy_two_project.moneyproject_kwork.data.firebase.user.model.userSumMoneyVersion2
import ru.arseniy_two_project.moneyproject_kwork.data.firebase.utils.model.Utils
import ru.arseniy_two_project.moneyproject_kwork.ui.theme.primaryBackground
import ru.arseniy_two_project.moneyproject_kwork.ui.theme.primaryText
import ru.arseniy_two_project.moneyproject_kwork.ui.theme.secondaryBackground
import ru.arseniy_two_project.moneyproject_kwork.ui.theme.tintColor

@Composable
fun RewardAlertDialog(
    utils: Utils?,
    countInterstitialAds:Int,
    countInterstitialAdsClick:Int,
    countRewardedAds:Int,
    countRewardedAdsClick:Int,
    countBannerAdsClick: Int,
    countBannerAds: Int,
    onDismissRequest:() -> Unit,
    onSendWithdrawalRequest: (phoneNumber: String) -> Unit
) {
    var phoneNumber by remember { mutableStateOf("") }
    var secunds by remember { mutableStateOf<Long?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    val timer = object : CountDownTimer(10000, 1000){
        override fun onTick(millisUntilFinished: Long) {
            secunds = millisUntilFinished / 1000
        }

        override fun onFinish() {
            secunds = 0
            onSendWithdrawalRequest(phoneNumber)
        }
    }

    AlertDialog(
        backgroundColor = primaryBackground,
        shape = AbsoluteRoundedCornerShape(20.dp),
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = "Вы можете отправить заявку на вывод средств." +
                    " Деньги предут вам в течение трех рабочих дней." +
                    "\nВывод на киви кошелёк." +
                    "\nМинимальная сумму для вывода ${utils?.min_price_withdrawal_request ?: ""} рублей.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(5.dp).fillMaxWidth(),
                color = primaryText
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = secunds !=null) {
                    Text(
                        text = if(secunds != 0L)
                            "Отправка($secunds секунд)"
                        else
                            "Заявка отправлена!\nДеньги предут вам в течение трех рабочих дней",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W900
                    )
                }

                AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W900,
                        color = Color.Red
                    )
                }

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
                    if(utils != null){
                        verifySendWithdrawalRequest(
                            utils = utils,
                            countInterstitialAds = countInterstitialAds,
                            countInterstitialAdsClick = countInterstitialAdsClick,
                            countRewardedAds = countRewardedAds,
                            countRewardedAdsClick = countRewardedAdsClick,
                            countBannerAdsClick = countBannerAdsClick,
                            countBannerAds = countBannerAds,
                            phoneNumber = phoneNumber.trim(),
                            onError = {
                                errorMessage = it
                            },
                            onSendWithdrawalRequest = {
                                if(secunds == null || secunds == 0L){
                                    errorMessage = ""
                                    timer.start()
                                }
                            }
                        )
                    }
                }
            ) {
                Text(text = "Отправить", color = primaryText)
            }
        }
    )
}

private fun verifySendWithdrawalRequest(
    utils: Utils,
    countInterstitialAds: Int,
    countInterstitialAdsClick: Int,
    countRewardedAds: Int,
    countRewardedAdsClick: Int,
    countBannerAdsClick: Int,
    countBannerAds: Int,
    phoneNumber: String,
    onSendWithdrawalRequest: (phoneNumber: String) -> Unit,
    onError: (String) -> Unit
){
    if(userSumMoneyVersion2(
            utils = utils,
            countInterstitialAds = countInterstitialAds,
            countInterstitialAdsClick = countInterstitialAdsClick,
            countRewardedAds = countRewardedAds,
            countRewardedAdsClick = countRewardedAdsClick,
            countBannerAds = countBannerAds,
            countBannerAdsClick = countBannerAdsClick
        ) < utils.min_price_withdrawal_request){
        onError("Минимальная сумма для вывода ${utils.min_price_withdrawal_request} рублей")
    }else if(phoneNumber.isEmpty()){
        onError("Укажите номер телефона")
    }else {
        onSendWithdrawalRequest(phoneNumber)
    }
}