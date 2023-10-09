package com.example.evagnelyrics.ui.theme.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evagnelyrics.R

@Composable
fun DiscJockeyBehaviour(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    coverImage: @Composable () -> Unit
) {
    var stoppedDegree by remember {
        mutableFloatStateOf(0f)
    }

    val stopAnimation by animateFloatAsState(
        targetValue = if (!isPlaying) 0f else stoppedDegree,
        spring(Spring.DampingRatioMediumBouncy), label = "stop animation"
    )

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .rotate(stopAnimation)
                .clickable { onClick() }
        ) {
            coverImage()
        }
        if (isPlaying) {
            val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")

            val unstoppableDegrees by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    tween(durationMillis = 2500, easing = LinearEasing),
                ), label = "infinite rotation"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .rotate(unstoppableDegrees)
                    .clickable {
                        stoppedDegree = unstoppableDegrees
                        onClick()
                    }
            ) {
                coverImage()
            }
        }
    }
}

@Preview
@Composable
fun DiscJokerPrev() {

    val (play, onPlay) = remember {
        mutableStateOf(true)
    }

    Row(Modifier.fillMaxWidth()) {
        DiscJockeyBehaviour(
            isPlaying = play,
            onClick = { onPlay(!play) },
            modifier = Modifier
                .height(200.dp)
                .aspectRatio(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.avicii_cover),
                contentDescription = "music cover",
                contentScale = ContentScale.Crop
            )
        }
    }
}