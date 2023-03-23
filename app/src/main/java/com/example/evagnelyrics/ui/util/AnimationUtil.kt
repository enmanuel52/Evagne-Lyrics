package com.example.evagnelyrics.ui.util

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin

/**
 * AnimatedVisibility for slide from where edge and hide from where edge to*/
@Composable
fun SlideInOutFrom(
    where: Where,
    modifier: Modifier = Modifier,
    visibleState: MutableTransitionState<Boolean>,
    delayMillis: Int = 0,
    durationMillis: Int = 400,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = when (where) {
            is Where.Horizontal -> {
                slideInHorizontally(
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis
                    )
                ) {
                    when (where.edge) {
                        Horizontally.Start -> -it
                        Horizontally.End -> it
                    }
                } + fadeIn(
                    tween(
                        durationMillis = durationMillis, delayMillis = delayMillis
                    )
                )
            }
            is Where.Vertical -> {
                slideInVertically(
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis
                    )
                ) {
                    when (where.edge) {
                        Vertically.Top -> -it
                        Vertically.Bottom -> it
                    }
                } + fadeIn(
                    tween(
                        durationMillis = durationMillis, delayMillis = delayMillis
                    )
                )
            }
        },
        exit = when (where) {
            is Where.Horizontal -> {
                slideOutHorizontally(
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis
                    )
                ) {
                    when (where.edge) {
                        Horizontally.Start -> -it
                        Horizontally.End -> it
                    }
                } + fadeOut(
                    tween(
                        durationMillis = durationMillis, delayMillis = delayMillis
                    )
                )
            }
            is Where.Vertical -> {
                slideOutVertically(
                    tween(
                        durationMillis = durationMillis,
                        delayMillis = delayMillis
                    )
                ) {
                    when (where.edge) {
                        Vertically.Top -> -it
                        Vertically.Bottom -> it
                    }
                } + fadeOut(
                    tween(
                        durationMillis = durationMillis, delayMillis = delayMillis
                    )
                )
            }
        },
    ) {
        content()
    }
}

sealed interface Where {
    data class Vertical(val edge: Vertically) : Where

    data class Horizontal(val edge: Horizontally) : Where
}

enum class Vertically { Top, Bottom }

enum class Horizontally { Start, End }

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
    modifier: Modifier = Modifier,
    durationMillis: Int = 400,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = scaleIn(
            tween(durationMillis)
        ) + fadeIn(tween(250)),
        exit = scaleOut() + fadeOut(),
        content = content
    )
}