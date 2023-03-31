package sinelix.sportapp.ui.view

import androidx.compose.material.MaterialTheme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import sinelix.sportapp.data.news.model.News
import sinelix.sportapp.utils.openBrowser

@ExperimentalMaterialApi
@Composable
fun NewsCard(
    news: News,
) {
    val context = LocalContext.current

    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            ),
        onClick = {
            expandedState = !expandedState
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(news.imageUrl != null && news.imageUrl.isNotEmpty()){
                    Image(
                        painter = rememberGlidePainter(news.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(AbsoluteRoundedCornerShape(15.dp))
                            .size(120.dp)
                            .padding(10.dp)
                    )
                }

                Text(
                    modifier = Modifier
                        .weight(6f),
                    text = news.title,
                    fontSize = MaterialTheme.typography.h6.fontSize,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                IconButton(
                    modifier = Modifier
                        .weight(1f)
                        .alpha(ContentAlpha.medium)
                        .rotate(rotationState),
                    onClick = {
                        expandedState = !expandedState
                    }) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )
                }
            }
            if (expandedState) {

                Text(
                    text = news.title,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W900,
                    modifier = Modifier.padding(3.dp)
                )

                Text(
                    text = news.description,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(3.dp)
                )

                TextButton(
                    modifier = Modifier
                        .padding(3.dp)
                        .align(Alignment.End),
                    onClick = {
                        context.openBrowser(news.webUrl)
                    }
                ) {
                    Text(
                        text = "More ->",
                        color = Color.White
                    )
                }
            }
        }
    }
}