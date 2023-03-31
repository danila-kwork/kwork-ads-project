package copa.gol.bet.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import copa.gol.bet.app.ui.view.BaseLottieAnimation
import copa.gol.bet.app.ui.view.LottieAnimationType

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) { BaseLottieAnimation(type = LottieAnimationType.LOADING) }
}