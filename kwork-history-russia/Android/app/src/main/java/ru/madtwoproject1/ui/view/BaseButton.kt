package ru.madtwoproject1.ui.view

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.madtwoproject1.ui.theme.primaryText
import ru.madtwoproject1.R

@Composable
fun BaseButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick:() -> Unit
) {
    val context = LocalContext.current
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    Box(
        modifier = modifier.padding(5.dp).clickable { onClick() }
    ) {
        Image(
            bitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(context.resources, R.drawable.main_button),
                (screenWidthDp / 1.8).toInt(),
                (screenHeightDp / 12).toInt(),
                false
            ).asImageBitmap(),
            contentDescription = null,
            modifier = Modifier
                .size(
                    width = (screenWidthDp / 1.8).dp,
                    height = (screenHeightDp / 12).dp
                )
        )

        Column(
            modifier = Modifier
                .size(
                    width = (screenWidthDp / 1.8).dp,
                    height = (screenHeightDp / 12).dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = primaryText,
                textAlign = TextAlign.Center
            )
        }
    }
}