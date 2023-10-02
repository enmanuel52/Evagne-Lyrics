package com.example.evagnelyrics.ui.util

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * AnimatedVisibility for slide from where edge and hide from where edge to
 * @param visibleState [MutableTransitionState] of type [Boolean]*/
@Composable
fun SlideInOutFrom(
    where: Where,
    modifier: Modifier = Modifier,
    visibleState: MutableTransitionState<Boolean>,
    delayMillis: Int = 0,
    durationMillis: Int = 400,
    hasBounce: Boolean = false,
    dampingRatio: Float = 1f,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = when (where) {
            is Where.Horizontal -> {
                slideInHorizontally(
                    if (hasBounce) spring(dampingRatio, Spring.StiffnessLow)
                    else tween(durationMillis, delayMillis)
                ) {
                    when (where.edge) {
                        Horizontally.Start -> -it
                        Horizontally.End -> it
                    }
                } /*+ fadeIn(
                    tween(
                        durationMillis = durationMillis, delayMillis = delayMillis
                    )
                )*/
            }
            is Where.Vertical -> {
                slideInVertically(
                    if (hasBounce) spring(dampingRatio, Spring.StiffnessLow)
                    else tween(durationMillis, delayMillis)
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

/**
 * AnimatedVisibility for slide from where edge and hide from where edge to
 * @param visible [Boolean]
 * @param dampingRatio [Float] must be between 0 and 1, the more closer of 0 more bounce*/
@Composable
fun SlideInOutFrom(
    where: Where,
    modifier: Modifier = Modifier,
    visible: Boolean,
    delayMillis: Int = 0,
    durationMillis: Int = 400,
    hasBounce: Boolean = false,
    dampingRatio: Float = 1f,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = when (where) {
            is Where.Horizontal -> {
                slideInHorizontally(
                    if (hasBounce) spring(dampingRatio, Spring.StiffnessLow)
                    else tween(durationMillis, delayMillis)
                ) {
                    when (where.edge) {
                        Horizontally.Start -> -it
                        Horizontally.End -> it
                    }
                } /*+ fadeIn(
                    tween(
                        durationMillis = durationMillis, delayMillis = delayMillis
                    )
                )*/
            }
            is Where.Vertical -> {
                slideInVertically(
                    if (hasBounce) spring(dampingRatio, Spring.StiffnessLow)
                    else tween(durationMillis, delayMillis)
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
            tween(durationMillis, easing = LinearEasing)
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
    hasBounce: Boolean = false,
    delayMillis: Int = 0,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = scaleIn(
            if (hasBounce) spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
            else tween(durationMillis, delayMillis)
        ),
        exit = scaleOut() + fadeOut(),
        content = content
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ScaleFromCenter(
    visible: Boolean,
    modifier: Modifier = Modifier,
    durationMillis: Int = 400,
    hasBounce: Boolean = false,
    delayMillis: Int = 0,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible,
        modifier = modifier,
        enter = scaleIn(
            if (hasBounce) spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow)
            else tween(durationMillis, delayMillis)
        ),
        exit = scaleOut() + fadeOut(),
        content = content
    )
}