package ru.fourthproject.guessthenumber.ui.screens.settingsScreen

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.fourthproject.guessthenumber.data.firebase.utils.UtilsDataStore
import ru.fourthproject.guessthenumber.data.firebase.utils.model.Utils
import ru.fourthproject.guessthenumber.ui.theme.primaryBackground
import ru.fourthproject.guessthenumber.ui.theme.primaryText
import ru.fourthproject.guessthenumber.ui.theme.tintColor

@Composable
fun SettingsScreen(
    navController: NavController
) {

    val utilsDataStore = remember(::UtilsDataStore)

    var bannerYandexAdsId by remember { mutableStateOf("") }
    var interstitialAdsClickPrice by remember { mutableStateOf("") }
    var interstitialAdsPrice by remember { mutableStateOf("") }
    var interstitialYandexAdsId by remember { mutableStateOf("") }
    var minPriceWithdrawalRequest by remember { mutableStateOf("") }
    var rewardedAdsClick by remember { mutableStateOf("") }
    var rewardedAdsPrice by remember { mutableStateOf("") }
    var rewardedYandexAdsId by remember { mutableStateOf("") }
    var bannerAdsPrice by remember { mutableStateOf("") }
    var bannerAdsClickPrice by remember { mutableStateOf("") }

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
            bannerAdsPrice = utils.banner_ads_price.toString()
            bannerAdsClickPrice = utils.banner_ads_click_price.toString()
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
                                banner_ads_click_price = bannerAdsClickPrice.toDouble(),
                                banner_ads_price = bannerAdsPrice.toDouble()
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