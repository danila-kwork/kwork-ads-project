package ru.russian_language_quiz.ui.screens.fonetikaScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.TextView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import org.joda.time.Period
import ru.russian_language_quiz.R
import ru.russian_language_quiz.data.fonetika.FonetikaDataStore
import ru.russian_language_quiz.data.fonetika.model.Fonetika
import ru.russian_language_quiz.data.user.UserDataStore
import ru.russian_language_quiz.data.user.model.User
import ru.russian_language_quiz.data.user.model.userSumMoneyVersion2
import ru.russian_language_quiz.data.utils.UtilsDataStore
import ru.russian_language_quiz.data.utils.model.Utils
import ru.russian_language_quiz.ui.theme.primaryText
import ru.russian_language_quiz.ui.theme.secondaryBackground
import ru.russian_language_quiz.ui.theme.tintColor
import ru.russian_language_quiz.ui.view.Board
import ru.russian_language_quiz.ui.view.LoadingUi
import ru.russian_language_quiz.yandexAds.RewardedYandexAds

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FonetikaScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var fonetikas by remember { mutableStateOf(listOf<Fonetika>()) }
    var fonetikaItem by remember { mutableStateOf<Fonetika?>(null) }
    var webView by remember { mutableStateOf(false) }
    val fonetikaDataStore = remember(::FonetikaDataStore)

    var searchText by remember { mutableStateOf("") }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    var utils by remember { mutableStateOf<Utils?>(null) }

    val rewardedYandexAds = remember {
        RewardedYandexAds(context, onDismissed = { adClickedDate, returnedToDate, rewarded ->
            if(adClickedDate != null && returnedToDate != null && rewarded && user != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountRewardedAdsClick(user!!.countRewardedAdsClick + 1)
                }else {
                    userDataStore.updateCountRewardedAds(user!!.countRewardedAds + 1)
                }
            } else if(rewarded && user != null){
                userDataStore.updateCountRewardedAds(user!!.countRewardedAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        fonetikaDataStore.getAll { fonetikas = it }
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

    LaunchedEffect(key1 = searchText, block = {
        if(searchText.isNotEmpty()){
            fonetikas = fonetikas.filter {
                it.title.contains(searchText)
            }
        }
    })

    BackHandler {
        if(fonetikaItem == null)
            navController.navigateUp()
        else
            if(webView)
                webView = false
            else
                fonetikaItem = null
    }

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

    if(fonetikas.isEmpty()){
        LoadingUi()
    }

    LazyVerticalGrid(GridCells.Fixed(2)) {
        if(fonetikaItem == null){

            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                if(fonetikas.isNotEmpty()){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
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
                                Text(text = "Поиск", color = primaryText)
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = primaryText
                                )
                            }
                        )
                    }
                }
            }

            items(fonetikas){ item ->
                Card(
                    modifier = Modifier
                        .padding(5.dp),
                    backgroundColor = secondaryBackground,
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        fonetikaItem = item
                        rewardedYandexAds.show()
                    }
                ) {
                    Column {
                        Text(
                            text = item.title,
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth(),
                            color = primaryText,
                            fontWeight = FontWeight.W900,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }else {
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                Column {
                    if(webView){
                        WebView(
                            state = rememberWebViewState(fonetikaItem!!.url),
                            modifier = Modifier.fillMaxSize()
                        )
                    }else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Board(
                                modifier = Modifier.padding(10.dp),
                                text = userSumMoneyVersion2(
                                    utils = utils,
                                    countBannerAds = user?.countBannerAds ?: 0,
                                    countBannerAdsClick = user?.countBannerAdsClick ?: 0,
                                    countInterstitialAds = user?.countInterstitialAds ?: 0,
                                    countInterstitialAdsClick = user?.countInterstitialAdsClick ?: 0,
                                    countRewardedAds = user?.countRewardedAds ?: 0,
                                    countRewardedAdsClick = user?.countRewardedAdsClick ?: 0,
                                    achievementPrice = user?.achievementPrice ?: 0.0,
                                    referralLinkMoney = user?.referralLinkMoney ?: 0.0,
                                    giftMoney = user?.giftMoney ?: 0.0,
                                ).toString(),
                                width = (screenWidthDp / 2).toDouble(),
                                height = (screenHeightDp / 10).toDouble()
                            )
                        }

                        AndroidView(
                            modifier = Modifier.fillMaxSize().padding(10.dp),
                            factory = { context -> TextView(context) },
                            update = {
                                it.text = HtmlCompat.fromHtml(
                                    fonetikaItem?.content ?: "",
                                    HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}