package com.MadJon.PINPNG.ui.screens.questionsScreen.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.MadJon.PINPNG.data.questions.model.Question
import com.MadJon.PINPNG.ui.theme.primaryText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuestionList(
    questionList: List<Question>,
    onClick: (Question) -> Unit
) {
    LazyVerticalStaggeredGrid(columns = StaggeredGridCells.Fixed(2)){
        items(questionList){ question ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(5.dp)
                    .clickable { onClick(question) }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = question.image),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(AbsoluteRoundedCornerShape(20.dp))
                        .size(100.dp)
                )

                Text(
                    text = question.title,
                    modifier = Modifier.padding(5.dp),
                    color = primaryText,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}