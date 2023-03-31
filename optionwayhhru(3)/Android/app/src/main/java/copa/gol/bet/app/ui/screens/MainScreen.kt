package copa.gol.bet.app.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.navigation.NavController
import copa.gol.bet.app.data.news.NewsDataStore
import copa.gol.bet.app.data.news.model.News
import copa.gol.bet.app.ui.view.NewsCard

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(
    navController: NavController
) {

    val newsDataStore = remember(::NewsDataStore)
    var newsList by remember { mutableStateOf(emptyList<News>()) }

    LaunchedEffect(key1 = Unit, block = {
        newsDataStore.getNewsList { newsList = it }
    })

    if(newsList.isEmpty()){
        LoadingScreen()
    }else {
        LazyColumn {
            items(
                newsList,
                key = { it.id }
            ){ news ->
                NewsCard(news = news) {
                    navController.navigate("news_more/${news.id}")
                }
            }
        }
    }
}