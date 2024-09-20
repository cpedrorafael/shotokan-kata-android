package com.guayaba.shotokankata.ui.philosophy

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.guayaba.shotokankata.R
import com.guayaba.shotokankata.ui.navigation.Routes
import kotlinx.coroutines.delay

@Composable
fun NijuKun(viewModel: PhilosophyViewModel, navController: NavHostController, nextRoute: Routes) {
    val principle = remember {
        viewModel.getNijuKun()
    }

    val timerDuration = 2000L

    LaunchedEffect(Unit) {
        delay(timerDuration)
        navController.navigate(nextRoute.route)
    }

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Create an infinite transition
    val infiniteTransition = rememberInfiniteTransition(label = "")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    val iconSize = screenWidth * .9f

    Box(Modifier.fillMaxSize()) {
        Icon(
            painter = painterResource(id = R.drawable.mushin_tiger),
            contentDescription = "Mushin",
            modifier = Modifier
                .align(Alignment.Center)
                .size(iconSize * scale),
            tint = if (isSystemInDarkTheme()) Color.White else Color.Black,
        )

        Column(Modifier.align(BiasAlignment(0f, 0.6f))) {
           AnimatedText(text = principle.description)
        }

    }
}

@Composable
fun AnimatedText(text: String) {
    var targetAlpha by remember { mutableFloatStateOf(0f) }

    // Animate alpha from 0f to 1f
    val alpha by animateFloatAsState(
        targetValue = targetAlpha,
        animationSpec = tween(durationMillis = 1000) // Adjust duration as needed
    )

    // Trigger the animation when the composable enters the composition
    LaunchedEffect(Unit) {
        targetAlpha = 1f
    }

    Text(
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.graphicsLayer(alpha = alpha)
    )
}