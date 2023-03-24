package ru.madtwoproject1.ui.screens.achievementScreen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.madtwoproject1.R
import ru.madtwoproject1.data.achievement.AchievementDataStore
import ru.madtwoproject1.data.achievement.model.Achievement
import ru.madtwoproject1.data.achievement.model.AchievementType
import ru.madtwoproject1.data.user.UserDataStore
import ru.madtwoproject1.data.user.model.User
import ru.madtwoproject1.ui.theme.primaryText
import ru.madtwoproject1.ui.theme.tintColor
import ru.madtwoproject1.ui.view.CustomComponent
import ru.madtwoproject1.ui.view.LoadingUi

@Composable
fun AchievementScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp
    var achievementList by remember { mutableStateOf(emptyList<Achievement>()) }
    val achievementDataStore = remember(::AchievementDataStore)
    var achievementIndex by remember { mutableStateOf(0) }
    val scope = rememberCoroutineScope()
    val userDataStore = remember(::UserDataStore)
    var user by remember { mutableStateOf<User?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        userDataStore.get { user = it }
        delay(1000L)
        achievementDataStore.getList {
            achievementList = it.filter {
                it.id !in (user?.achievementIds?.map { it.id } ?: emptyList())
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

    if(user == null || achievementList.isEmpty()){
        LoadingUi()
    }

    if(user != null){

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val item = achievementList.getOrNull(achievementIndex)

            item?.let {

                Spacer(modifier = Modifier.height(100.dp))

                CustomComponent(
                    indicatorValue = when(item.type){
                        AchievementType.QUESTIONS -> user?.countQuestion ?: 0
                        AchievementType.INTERESTING_FACT -> user?.countInterestingFact ?: 0
                    },
                    maxIndicatorValue = item.count,
                    smallText = "из ${item.count}",
                    smallTextColor = primaryText,
                    foregroundIndicatorColor = tintColor
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = when(item.type){
                        AchievementType.QUESTIONS -> "Ответь на вопросы"
                        AchievementType.INTERESTING_FACT -> "Посмотри интересные факты"
                    },
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = primaryText,
                    fontWeight = FontWeight.W900,
                    fontSize = 25.sp
                )

                Text(
                    text = "Награда ${item.reward} рублей",
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = primaryText,
                    fontWeight = FontWeight.W900,
                    fontSize = 25.sp
                )

                val isReward = when(item.type){
                    AchievementType.QUESTIONS -> user!!.countQuestion >= item.count
                    AchievementType.INTERESTING_FACT -> user!!.countInterestingFact >= item.count
                }

                AnimatedVisibility(visible = isReward) {
                    Button(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth(),
                        shape = AbsoluteRoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = tintColor
                        ),
                        onClick = {
                            userDataStore.addAchievementId(item.id, user!!.achievementPrice + item.reward){
                                achievementDataStore.getList {
                                    achievementList = it.filter {
                                        it.id !in (user?.achievementIds?.map { it.id } ?: emptyList())
                                    }
                                }
                            }
                        }
                    ) {
                        Text(text = "Забрать награду",color = primaryText)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Bottom
            ) {
                if(achievementIndex != 0){
                    IconButton(onClick = {
                        scope.launch {
                            achievementIndex--
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.left),
                            contentDescription = null,
                            modifier = Modifier.size(150.dp)
                        )
                    }
                }

                if(achievementList.size-1 != achievementIndex){
                    IconButton(onClick = {
                        scope.launch {
                            achievementIndex++
                        }
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
    }
}