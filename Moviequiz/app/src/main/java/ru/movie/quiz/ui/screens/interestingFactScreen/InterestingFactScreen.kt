package ru.movie.quiz.ui.screens.interestingFactScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.delay
import org.joda.time.Period
import ru.movie.quiz.R
import ru.movie.quiz.common.items
import ru.movie.quiz.common.parseHtml
import ru.movie.quiz.data.interestingFacts.InterestingFactDataStore
import ru.movie.quiz.data.interestingFacts.model.InterestingFact
import ru.movie.quiz.data.user.UserDataStore
import ru.movie.quiz.data.user.model.User
import ru.movie.quiz.data.user.model.userSumMoneyVersion2
import ru.movie.quiz.data.utils.UtilsDataStore
import ru.movie.quiz.data.utils.model.Utils
import ru.movie.quiz.ui.theme.getRatingColor
import ru.movie.quiz.ui.theme.primaryText
import ru.movie.quiz.ui.theme.secondaryBackground
import ru.movie.quiz.ui.view.Board
import ru.movie.quiz.ui.view.LoadingUi
import ru.movie.quiz.yandexAds.RewardedYandexAds

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun InterestingFactScreen(
    navController: NavController,
    viewModel: InterestingFactViewModel = viewModel()
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    var user by remember { mutableStateOf<User?>(null) }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    val interestingFactDataStore = remember(::InterestingFactDataStore)
    var interestingFact by remember { mutableStateOf<List<InterestingFact>>(emptyList()) }
    var interestingFactIndex by remember { mutableStateOf(0) }
    var countInterestingFact by remember { mutableStateOf(0) }
    var filmId by remember { mutableStateOf<Int?>(null) }
    val films = viewModel.getFilmsTop.collectAsLazyPagingItems()

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

    BackHandler {
        if(filmId == null)
            navController.navigateUp()
        else
            filmId = null
    }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

    LaunchedEffect(key1 = filmId, block = {
        filmId?.let {
            interestingFact = interestingFactDataStore.getInterestingFact(it).items
        }
    })

    LaunchedEffect(key1 = countInterestingFact, block = {
        if(countInterestingFact % 5 == 0 && countInterestingFact != 0){
            rewardedYandexAds.show()
        }
    })

    LaunchedEffect(interestingFactIndex, filmId, block = {
        if(filmId != null){
            delay(1000L)
            user?.countInterestingFact?.let { userCountInterestingFact ->
                userDataStore.updateCountInterestingFact(userCountInterestingFact + 1)
            }
        }
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

    if(filmId != null){

        Column {
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
                    giftMoney = user?.giftMoney ?: 0.0
                ).toString(),
                width = (screenWidthDp / 2).toDouble(),
                height = (screenHeightDp / 10).toDouble()
            )
        }

        if(interestingFact.isNotEmpty()){
            val fact = interestingFact.getOrNull(interestingFactIndex)

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = fact?.text?.parseHtml() ?: "",
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(10.dp),
                    color = primaryText,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                IconButton(onClick = {
                    interestingFactIndex--
                    countInterestingFact++
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.left),
                        contentDescription = null,
                        modifier = Modifier.size(150.dp)
                    )
                }

                if(interestingFact.size-1 != interestingFactIndex){
                    IconButton(onClick = {
                        interestingFactIndex++
                        countInterestingFact++
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.right),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }
            }
        }
    }else {
        if(films.itemCount <= 0){
            LoadingUi()
        }

        LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)) {
            items(films){ item ->
                Card(
                    modifier = Modifier.padding(10.dp),
                    backgroundColor = secondaryBackground,
                    shape = AbsoluteRoundedCornerShape(15.dp),
                    onClick = {
                        filmId = item?.filmId
                    }
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box {
                            Image(
                                painter = rememberAsyncImagePainter(model = item?.posterUrl),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(5.dp)
                                    .size(150.dp)
                            )

                            Card(
                                backgroundColor = item!!.rating.toDouble().getRatingColor(),
                                shape = AbsoluteRoundedCornerShape(15.dp)
                            ) {
                                Text(
                                    text = item.rating,
                                    color = primaryText,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }
                        }

                        Text(
                            text = item?.nameRu.toString(),
                            modifier = Modifier.padding(5.dp),
                            color = primaryText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}