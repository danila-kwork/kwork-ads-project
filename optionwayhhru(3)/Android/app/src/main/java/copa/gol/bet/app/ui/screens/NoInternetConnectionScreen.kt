package copa.gol.bet.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import copa.gol.bet.app.ui.view.BaseLottieAnimation
import copa.gol.bet.app.ui.view.LottieAnimationType

@Composable
fun NoInternetConnectionScreen(

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BaseLottieAnimation(
            type = LottieAnimationType.NO_INTERNET_CONNECTION,
            modifier = Modifier.size(300.dp).padding(5.dp)
        )

        Text(
            text = "Network connection is required to continue",
            fontWeight = FontWeight.W900,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(5.dp)
        )
    }
}