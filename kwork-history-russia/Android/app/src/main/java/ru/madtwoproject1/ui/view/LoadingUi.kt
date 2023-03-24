package ru.madtwoproject1.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import ru.madtwoproject1.ui.theme.tintColor

@Composable
fun LoadingUi() {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp.dp
    val screenHeightDp = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = Modifier.size(
            width = screenWidthDp,
            height = screenHeightDp
        )
    ) {
        CircularProgressIndicator(
            color = tintColor,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}