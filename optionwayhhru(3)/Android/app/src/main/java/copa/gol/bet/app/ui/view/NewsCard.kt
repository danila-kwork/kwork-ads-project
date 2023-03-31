package copa.gol.bet.app.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.glide.rememberGlidePainter
import copa.gol.bet.app.data.news.model.News

@ExperimentalMaterialApi
@Composable
fun NewsCard(
    news: News,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = onClick,
        shape = AbsoluteRoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(news.imageUrl != null && news.imageUrl.isNotEmpty()){
                Image(
                    painter = rememberGlidePainter(news.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .padding(10.dp)
                        .clip(AbsoluteRoundedCornerShape(15.dp))
                )
            }

            Text(
                modifier = Modifier
                    .weight(6f)
                    .padding(5.dp),
                text = news.title,
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}