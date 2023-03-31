package sinelix.sportapp.ui.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import sinelix.sportapp.data.news.NewsDataStore
import sinelix.sportapp.data.news.model.News
import sinelix.sportapp.ui.view.NewsCard

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen() {

    val newsDataStore = remember(::NewsDataStore)
    var newsList by remember { mutableStateOf(emptyList<News>()) }

    LaunchedEffect(key1 = Unit, block = {
        newsDataStore.getNewsList { newsList = it }
    })

    if(newsList.isEmpty()){
        LoadingScreen()
    }else {
        LazyColumn {
            items(newsList){ news ->
                NewsCard(news = news)
                Divider()
            }
        }
    }
}