package com.example.notes.ui.screens.settingsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.notes.LocalNavController
import com.example.notes.data.firebase.utils.UtilsDataStore
import com.example.notes.data.firebase.utils.model.Utils
import com.example.notes.ui.theme.primaryBackground
import com.example.notes.ui.theme.primaryText
import com.example.notes.ui.theme.tintColor

@Composable
fun SettingsScreen() {

    val navController = LocalNavController.current
    val utilsDataStore = remember(::UtilsDataStore)

    var bannerYandexAdsId by remember { mutableStateOf("") }
    var rewardedYandexAdsId by remember { mutableStateOf("") }
    var interstitialYandexAdsId by remember { mutableStateOf("") }
    var minPriceWithdrawalRequest by remember { mutableStateOf("") }

    var adsPrice by remember { mutableStateOf("") }
    var adsClickPrice by remember { mutableStateOf("") }
    var answersPrice by remember { mutableStateOf("") }
    var clickWatchAdsPrice by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        utilsDataStore.get(onSuccess = {  utils ->
            bannerYandexAdsId = utils.banner_yandex_ads_id
            interstitialYandexAdsId = utils.interstitial_yandex_ads_id
            minPriceWithdrawalRequest = utils.min_price_withdrawal_request.toString()
            rewardedYandexAdsId = utils.rewarded_yandex_ads_id
            adsPrice = utils.adsPrice.toString()
            adsClickPrice = utils.adsClickPrice.toString()
            answersPrice = utils.answersPrice.toString()
            clickWatchAdsPrice = utils.clickWatchAdsPrice.toString()
        })
    })

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = primaryBackground
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                BaseOutlinedTextField(
                    label = "За просмотр рекламы",
                    value = adsPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { adsPrice = it }
                )

                BaseOutlinedTextField(
                    label = "За переход на сайт",
                    value = adsClickPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { adsClickPrice = it }
                )

                BaseOutlinedTextField(
                    label = "За правильный ответ",
                    value = answersPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { answersPrice = it }
                )

                BaseOutlinedTextField(
                    label = "За просмотр рекламы (кнопка смотреть)",
                    value = clickWatchAdsPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { clickWatchAdsPrice = it }
                )

                BaseOutlinedTextField(
                    label = "Минимальная сумма для вывода",
                    value = minPriceWithdrawalRequest,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { minPriceWithdrawalRequest = it }
                )

                Divider(color = tintColor)

                BaseOutlinedTextField(
                    label = "Баннер id",
                    value = bannerYandexAdsId,
                    onValueChange = { bannerYandexAdsId = it }
                )

                BaseOutlinedTextField(
                    label = "Полноэкраная id",
                    value = interstitialYandexAdsId,
                    onValueChange = { interstitialYandexAdsId = it }
                )

                BaseOutlinedTextField(
                    label = "Видео id",
                    value = rewardedYandexAdsId,
                    onValueChange = { rewardedYandexAdsId = it }
                )

                Button(
                    modifier = Modifier.padding(10.dp),
                    onClick = {
                        utilsDataStore.create(
                            utils = Utils(
                                banner_yandex_ads_id = bannerYandexAdsId,
                                interstitial_yandex_ads_id = interstitialYandexAdsId,
                                min_price_withdrawal_request = minPriceWithdrawalRequest.toDouble(),
                                rewarded_yandex_ads_id = rewardedYandexAdsId,
                                adsPrice = adsPrice.toDouble(),
                                adsClickPrice = adsClickPrice.toDouble(),
                                answersPrice = answersPrice.toDouble(),
                                clickWatchAdsPrice = clickWatchAdsPrice.toDouble(),
                                banner_ads_click_price = 0.0
                            ),
                            onSuccess = { navController.navigateUp() }
                        )
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = tintColor)
                ) {
                    Text(text = "Сохранить", color = primaryText)
                }
            }
        }
    }
}

@Composable
private fun BaseOutlinedTextField(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    onValueChange: (String) -> Unit = {}
){
    Text(
        text = label,
        fontWeight = FontWeight.W900,
        modifier = Modifier.padding(5.dp)
    )

    OutlinedTextField(
        modifier = Modifier.padding(bottom = 5.dp, start = 5.dp),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = primaryBackground,
            textColor = primaryText
        )
    )
}