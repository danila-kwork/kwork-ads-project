package ru.biology_quiz.ui.screens.articlesScreen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import org.joda.time.Period
import ru.biology_quiz.R
import ru.biology_quiz.data.articles.ArticleDataStore
import ru.biology_quiz.data.articles.model.Article
import ru.biology_quiz.data.user.UserDataStore
import ru.biology_quiz.data.user.model.User
import ru.biology_quiz.data.user.model.userSumMoneyVersion2
import ru.biology_quiz.data.utils.UtilsDataStore
import ru.biology_quiz.data.utils.model.Utils
import ru.biology_quiz.ui.theme.primaryText
import ru.biology_quiz.ui.theme.secondaryBackground
import ru.biology_quiz.ui.theme.tintColor
import ru.biology_quiz.ui.view.Board
import ru.biology_quiz.ui.view.LoadingUi
import ru.biology_quiz.yandexAds.RewardedYandexAds

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArticlesScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var articles by remember { mutableStateOf(listOf<Article>()) }
    var articleItem by remember { mutableStateOf<Article?>(null) }
    var webView by remember { mutableStateOf(false) }
    val articlesDataStore = remember(::ArticleDataStore)

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
        articlesDataStore.getAll { articles = it }
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

    LaunchedEffect(key1 = searchText, block = {
        if(searchText.isNotEmpty()){
            articles = articles.filter {
                it.title.contains(searchText)
                        || it.author.contains(searchText)
                        || it.teacher.contains(searchText)
                        || it.html.contains(searchText)
            }
        }
    })

    BackHandler {
        if(articleItem == null)
            navController.navigateUp()
        else
            if(webView)
                webView = false
            else
                articleItem = null
    }

    Scaffold {
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

        if(articles.isEmpty()){
            LoadingUi()
        }

        LazyColumn {
            if(articleItem == null){

                item {
                    if(articles.isNotEmpty()){
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

                items(articles){ item ->
                    Card(
                        modifier = Modifier.padding(10.dp),
                        backgroundColor = secondaryBackground,
                        shape = AbsoluteRoundedCornerShape(15.dp),
                        onClick = {
                            articleItem = item
                            rewardedYandexAds.show()
                        }
                    ) {
                        Column {
                            Text(
                                text = item.title,
                                modifier = Modifier.padding(5.dp),
                                color = primaryText,
                                fontWeight = FontWeight.W900
                            )

                            Text(
                                text = item.author,
                                modifier = Modifier.padding(5.dp),
                                color = primaryText
                            )

                            Text(
                                text = item.teacher,
                                modifier = Modifier.padding(5.dp),
                                color = primaryText
                            )
                        }
                    }
                }
            }else {
                item {
                    if(webView){
                        WebView(
                            state = rememberWebViewState(articleItem!!.url),
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

                            TextButton(onClick = { webView = true }) {
                                Text(
                                    text = "Сайт ->",
                                    color = tintColor,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }

                        Text(
                            text = articleItem!!.html,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp),
                            color = primaryText,
                        )
                    }
                }
            }
        }
    }
}