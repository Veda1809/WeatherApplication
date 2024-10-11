package com.example1.weatherapplication.core.utils

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun LottieAnimationView(
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    @RawRes resId: Int,
    onComplete: () -> Unit = {}
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(resId))
    val animationState by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        iterations = iterations
    )

    LaunchedEffect(animationState) {
        if (animationState == 1.0f) {
            onComplete()
        }
    }

    LottieAnimation(
        composition = composition,
        progress = { animationState },
        modifier = modifier
    )
}
