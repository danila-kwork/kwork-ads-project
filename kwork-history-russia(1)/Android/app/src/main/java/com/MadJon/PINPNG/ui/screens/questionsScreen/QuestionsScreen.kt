package com.MadJon.PINPNG.ui.screens.questionsScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.joda.time.Period
import com.MadJon.PINPNG.R
import com.MadJon.PINPNG.data.questions.QuestionDataStore
import com.MadJon.PINPNG.data.questions.model.Question
import com.MadJon.PINPNG.data.user.UserDataStore
import com.MadJon.PINPNG.data.user.model.User
import com.MadJon.PINPNG.data.user.model.userSumMoneyVersion2
import com.MadJon.PINPNG.data.utils.UtilsDataStore
import com.MadJon.PINPNG.data.utils.model.Utils
import com.MadJon.PINPNG.ui.screens.questionsScreen.view.QuestionList
import com.MadJon.PINPNG.ui.theme.primaryText
import com.MadJon.PINPNG.ui.theme.tintColor
import com.MadJon.PINPNG.ui.view.Board
import com.MadJon.PINPNG.ui.view.LoadingUi
import com.MadJon.PINPNG.yandexAds.InterstitialYandexAds

@OptIn(ExperimentalPagerApi::class)
@Composable
fun QuestionsScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val pageState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val wordsDataStore = remember(::QuestionDataStore)
    var user by remember { mutableStateOf<User?>(null) }
    val userDataStore = remember(::UserDataStore)
    val utilsDataStore = remember(::UtilsDataStore)
    var utils by remember { mutableStateOf<Utils?>(null) }
    var questionList by remember { mutableStateOf(emptyList<Question>()) }
    var questionCurrent by remember { mutableStateOf<Question?>(null) }
    var userAnswer by remember { mutableStateOf("") }
    var countQuestions by remember { mutableStateOf(0) }

    val interstitialYandexAds = remember {
        InterstitialYandexAds(context, onDismissed = { adClickedDate, returnedToDate ->
            user ?: return@InterstitialYandexAds

            if(adClickedDate != null && returnedToDate != null){
                if((Period(adClickedDate, returnedToDate)).seconds >= 10){
                    userDataStore.updateCountInterstitialAdsClick(user!!.countInterstitialAdsClick + 1)
                }else {
                    userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
                }
            } else {
                userDataStore.updateCountInterstitialAds(user!!.countInterstitialAds + 1)
            }
        })
    }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        utilsDataStore.get({ utils = it })
    })

    LaunchedEffect(key1 = Unit, block = {
        delay(1000L)
        wordsDataStore.getQuestionList { questionList = it; countQuestions++ }
    })

    LaunchedEffect(key1 = countQuestions, block = {
        if(countQuestions % 4 == 0 && countQuestions != 0){
            interstitialYandexAds.show()
        }
    })

    Surface(
        color = Color(0xFF4479af)
    ) {
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
                    achievementPrice = user?.achievementPrice ?: 0.0
                ).toString(),
                width = (screenWidthDp / 2).toDouble(),
                height = (screenHeightDp / 10).toDouble()
            )

            if(questionList.isEmpty()){
                LoadingUi()
            }

            if(questionCurrent == null){
                QuestionList(
                    questionList = questionList,
                    onClick = { questionCurrent = it }
                )
            }else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    questionCurrent?.let { quest ->
                        HorizontalPager(
                            state = pageState,
                            count = quest.content.size,
                            userScrollEnabled = false
                        ) {
                            val questionContent = quest.content[it]
                            var currentAnswer by remember { mutableStateOf(questionContent.answers[0]) }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = questionContent.question,
                                    fontWeight = FontWeight.W900,
                                    modifier = Modifier.padding(5.dp),
                                    color = primaryText,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                )

                                Column(
                                    modifier = Modifier.selectableGroup()
                                ) {
                                    questionContent.answers.forEach { answer ->
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            RadioButton(
                                                selected = answer == currentAnswer,
                                                onClick = {
                                                    currentAnswer = answer; userAnswer = answer.answer
                                                }
                                            )

                                            TextButton(onClick = {
                                                currentAnswer = answer; userAnswer = answer.answer
                                            }) {
                                                Text(
                                                    text = answer.answer,
                                                    fontSize = 22.sp,
                                                    color = primaryText
                                                )
                                            }
                                        }
                                    }
                                }

                                Button(
                                    modifier = Modifier.padding(10.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = tintColor),
                                    onClick = {
                                        if(userAnswer.lowercase().trim() == questionContent.answer.lowercase().trim()){
                                            userAnswer = ""
                                            countQuestions++
                                            Toast.makeText(context, "Правильно !", Toast.LENGTH_SHORT).show()
                                            user?.countQuestion?.let { userDataStore.updateCountQuestion(it + 1) }

                                            if(quest.content.size-1 == pageState.currentPage){
                                                Toast.makeText(context, "Тест закончен", Toast.LENGTH_SHORT).show()
                                                navController.navigateUp()
                                            }else{
                                                scope.launch {
                                                    pageState.animateScrollToPage(pageState.currentPage+1)
                                                }
                                            }
                                        }else {
                                            Toast.makeText(context, "Не правильно", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                ) {
                                    Text(
                                        text = "Продолжить",
                                        color = primaryText
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}