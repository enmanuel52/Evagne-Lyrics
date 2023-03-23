package com.example.evagnelyrics.ui.util

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable


/**
 * AnimatedVisibility for slide from right and hide on right*/
@Composable
fun SlideFromRight(
    visibleState: MutableTransitionState<Boolean>,
    delayMillis: Int = 0,
    durationMillis: Int = 400,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(
            tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis
            )
        ) { it } + fadeIn(
            tween(
                durationMillis = durationMillis, delayMillis = delayMillis
            )
        ),
        exit = slideOutHorizontally(
            tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis
            )
        ) { it } + fadeOut(
            tween(
                durationMillis = durationMillis, delayMillis = delayMillis
            )
        ),
    ) {
        content()
    }
}

@Composable
fun SlideFromRight(
    visible: Boolean,
    delayMillis: Int = 0,
    durationMillis: Int = 400,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis
            )
        ) { it } + fadeIn(
            tween(
                durationMillis = durationMillis, delayMillis = delayMillis
            )
        ),
        exit = slideOutHorizontally(
            tween(
                durationMillis = durationMillis,
                delayMillis = delayMillis
            )
        ) { it } + fadeOut(
            tween(
                durationMillis = durationMillis, delayMillis = delayMillis
            )
        ),
    ) {
        content()
    }
}

@Composable
fun SlideFromLeft(
    visible: Boolean,
    durationMillis: Int,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            tween(durationMillis)
        ) { -it }, //+ fadeIn(tween(durationMillis))
        exit = fadeOut(
            tween(200)
        ),
    ) {
        content()
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScaleFromCenter(
    visibleState: MutableTransitionState<Boolean>,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = scaleIn(
            spring(
                Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessVeryLow,
            )
        ) + fadeIn(tween(300)),
        exit = scaleOut() + fadeOut(),
        content = content
    )
}