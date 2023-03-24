package ru.money_ads.moneyproject_kwork.ui.screens.settingsScreen

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
import ru.money_ads.moneyproject_kwork.LocalNavController
import ru.money_ads.moneyproject_kwork.data.firebase.utils.UtilsDataStore
import ru.money_ads.moneyproject_kwork.data.firebase.utils.model.Utils
import ru.money_ads.moneyproject_kwork.ui.theme.primaryBackground
import ru.money_ads.moneyproject_kwork.ui.theme.primaryText
import ru.money_ads.moneyproject_kwork.ui.theme.tintColor

@Composable
fun SettingsScreen() {

    val navController = LocalNavController.current
    val utilsDataStore = remember(::UtilsDataStore)

    var bannerYandexAdsId by remember { mutableStateOf("") }
    var interstitialAdsClickPrice by remember { mutableStateOf("") }
    var interstitialAdsPrice by remember { mutableStateOf("") }
    var interstitialYandexAdsId by remember { mutableStateOf("") }
    var minPriceWithdrawalRequest by remember { mutableStateOf("") }
    var rewardedAdsClick by remember { mutableStateOf("") }
    var rewardedAdsPrice by remember { mutableStateOf("") }
    var rewardedYandexAdsId by remember { mutableStateOf("") }
    var bannerAdsClickPrice by remember { mutableStateOf("") }
    var bannerAdsPrice by remember { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, block = {
        utilsDataStore.get(onSuccess = {  utils ->
            bannerYandexAdsId = utils.banner_yandex_ads_id
            interstitialAdsClickPrice = utils.interstitial_ads_click_price.toString()
            interstitialAdsPrice = utils.interstitial_ads_price.toString()
            interstitialYandexAdsId = utils.interstitial_yandex_ads_id
            minPriceWithdrawalRequest = utils.min_price_withdrawal_request.toString()
            rewardedAdsClick = utils.rewarded_ads_click.toString()
            rewardedAdsPrice = utils.rewarded_ads_price.toString()
            rewardedYandexAdsId = utils.rewarded_yandex_ads_id
            bannerAdsClickPrice = utils.banner_ads_click_price.toString()
            bannerAdsPrice = utils.banner_ads_price.toString()
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
                    label = "Полноэкраная (с переходом)",
                    value = interstitialAdsClickPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { interstitialAdsClickPrice = it }
                )

                BaseOutlinedTextField(
                    label = "Полноэкраная (без перехода)",
                    value = interstitialAdsPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { interstitialAdsPrice = it }
                )

                BaseOutlinedTextField(
                    label = "Видео (с перехода)",
                    value = rewardedAdsClick,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { rewardedAdsClick = it }
                )

                BaseOutlinedTextField(
                    label = "Видео (без перехода)",
                    value = rewardedAdsPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { rewardedAdsPrice = it }
                )

                BaseOutlinedTextField(
                    label = "Баннер (с перехода больше 10 секунд)",
                    value = bannerAdsClickPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { bannerAdsClickPrice = it }
                )

                BaseOutlinedTextField(
                    label = "Баннер (с перехода меньше 10 секунд)",
                    value = bannerAdsPrice,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = { bannerAdsPrice = it }
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
                                interstitial_ads_click_price = interstitialAdsClickPrice.toDouble(),
                                interstitial_ads_price = interstitialAdsPrice.toDouble(),
                                interstitial_yandex_ads_id = interstitialYandexAdsId,
                                min_price_withdrawal_request = minPriceWithdrawalRequest.toDouble(),
                                rewarded_ads_click = rewardedAdsClick.toDouble(),
                                rewarded_ads_price = rewardedAdsPrice.toDouble(),
                                rewarded_yandex_ads_id = rewardedYandexAdsId,
                                banner_ads_price = bannerAdsPrice.toDouble(),
                                banner_ads_click_price = bannerAdsClickPrice.toDouble()
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
            backgroundColor = primaryBackground
        )
    )
}