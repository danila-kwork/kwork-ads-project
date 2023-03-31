package copa.gol.bet.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import copa.gol.bet.app.data.news.NewsDataStore
import copa.gol.bet.app.data.news.model.News
import copa.gol.bet.app.utils.openBrowser

@Composable
fun NewsMoreScreen(
    id: String
) {
    val context = LocalContext.current

    val newsDataStore = remember(::NewsDataStore)
    var news by remember { mutableStateOf<News?>(null) }

    LaunchedEffect(key1 = Unit, block = {
        newsDataStore.getById(id){ news = it }
    })

    if(news != null){
        LazyColumn {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(news!!.imageUrl != null && news!!.imageUrl!!.isNotEmpty()){
                        Image(
                            painter = rememberGlidePainter(news!!.imageUrl),
                            contentDescription = null,
                            modifier = Modifier
                                .size(300.dp)
                                .padding(10.dp)
                                .clip(AbsoluteRoundedCornerShape(25.dp))
                        )
                    }

                    Text(
                        modifier = Modifier.padding(5.dp),
                        text = news!!.title,
                        fontSize = MaterialTheme.typography.h6.fontSize,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    modifier = Modifier.padding(15.dp),
                    text = if(news!!.fullDescription == "null" || news!!.fullDescription.isEmpty())
                        news!!.description
                    else
                        news!!.fullDescription,
                )

                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            context.openBrowser(news!!.webUrl)
                        }
                    ) {
                        Text(
                            text = "More ->",
                            modifier = Modifier.padding(5.dp),
                            color = Color(0xFFDAAB03)
                        )
                    }
                }
            }
        }
    }else {
        LoadingScreen()
    }
}