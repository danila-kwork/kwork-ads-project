package ru.maproject.qrcode.ui.screens.mainScreen.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.maproject.qrcode.ui.theme.primaryBackground
import ru.maproject.qrcode.ui.theme.primaryText

@Composable
fun InfoAlertDialog(
    info: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        backgroundColor = primaryBackground,
        shape = AbsoluteRoundedCornerShape(20.dp),
        onDismissRequest = onDismissRequest,
        buttons = {
            Text(
                text = info,
                color = primaryText,
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    )
}