package com.example.evagnelyrics.ui.screen.main

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.MainViewModel
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextStyle
import com.example.evagnelyrics.ui.util.Horizontally
import com.example.evagnelyrics.ui.util.ScaleFromCenter
import com.example.evagnelyrics.ui.util.SlideInOutFrom
import com.example.evagnelyrics.ui.util.Where
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    keepSplash: () -> Unit,
    navController: NavHostController = LocalNavController.current!!,
    mainViewModel: MainViewModel = koinViewModel()
) {
    val onBack by remember {
        mutableStateOf(mainViewModel.previous() != null)
    }

    val visibleState = MutableTransitionState(onBack).apply {
        if (!onBack) {
            targetState = true
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                mainViewModel.pushScreen(Route.Main)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val smallDimen = MaterialTheme.dimen.small
    val mediumDimen = MaterialTheme.dimen.medium

    val constraints = ConstraintSet {
        val byDevRef = createRefFor(BY_DEV_ID)
        val photoRef = createRefFor(PHOTO_ID)
        val listRef = createRefFor(LIST_ID)
        val titleRef = createRefFor(TITLE_ID)
        val topGuideLine35 = createGuidelineFromTop(0.35f)
        val topGuideLine45 = createGuidelineFromTop(0.45f)
        val topGuideLine90 = createGuidelineFromTop(0.90f)

        constrain(photoRef) {
            top.linkTo(parent.top, mediumDimen)
            bottom.linkTo(topGuideLine35, margin = smallDimen)
            height = Dimension.fillToConstraints
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(titleRef) {
            top.linkTo(photoRef.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(topGuideLine45)
        }

        constrain(listRef) {
            top.linkTo(topGuideLine45)
            bottom.linkTo(topGuideLine90)
            height = Dimension.fillToConstraints
            start.linkTo(parent.start, margin = smallDimen)
            end.linkTo(parent.end, margin = smallDimen)
        }

        constrain(byDevRef) {
            bottom.linkTo(parent.bottom, margin = mediumDimen)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize(),
        constraintSet = constraints
    ) {
        SideEffect(keepSplash)

        ScaleFromCenter(
            modifier = Modifier.layoutId(PHOTO_ID),
            durationMillis = 500,
            visibleState = visibleState,
        ) {
            Image(
                painter = painterResource(R.drawable.img3_7228_crop),
                contentDescription = "front",
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(
                        MaterialTheme.dimen.verySmall,
                        MaterialTheme.colors.primaryVariant,
                        RoundedCornerShape(50)
                    )
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )
        }

        AnimatedVisibility(
            visibleState = visibleState,
            enter = fadeIn(tween(500)),
            exit = fadeOut(),
            modifier = Modifier.layoutId(TITLE_ID),
        ) {
            EvText(
                resource = R.string.app_name,
                style = EvTextStyle.Head,
                color = MaterialTheme.colors.primaryVariant,
            )
        }

        SlideInOutFrom(
            where = Where.Horizontal(Horizontally.End),
            modifier = Modifier
                .layoutId(LIST_ID),
            visibleState = visibleState,
            durationMillis = 500,
            delayMillis = 100,
        ) {
            LazyRow(
                modifier = Modifier
                    .padding(end = MaterialTheme.dimen.verySmall)
            ) {
                val pictures = listOf(R.drawable.img2_7190, R.drawable.img4_7236)
                val titles = listOf(R.string.songs, R.string.title_wallpapers)
                val destinations = listOf(Route.List.toString(), Route.Gallery.toString())
                items(count = 2) { index ->
                    MainCard(image = pictures[index], title = titles[index]) {
                        navController.navigate(destinations[index])
                    }
                }
            }
        }

        AnimatedVisibility(
            visibleState = visibleState,
            modifier = Modifier
                .layoutId(BY_DEV_ID),
            enter = slideInVertically(tween(500)) { it },
            exit = slideOutVertically { it }
        ) {
            EvText(
                resource = R.string.by_dev,
                color = MaterialTheme.colors.primaryVariant,
                modifier = Modifier
                    .padding(MaterialTheme.dimen.large)
            )
        }
    }


}

@Composable
fun MainCard(
    @DrawableRes image: Int,
    @StringRes title: Int,
    navigate: () -> Unit
) {
    Card(
        elevation = MaterialTheme.dimen.verySmall,
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(0.5625f)
            .padding(vertical = MaterialTheme.dimen.small)
            .padding(start = MaterialTheme.dimen.small)
            .clip(RoundedCornerShape(8))
            .clickable { navigate() }
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = image),
                contentDescription = stringResource(id = title) + "image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(6f)
                    .clip(RoundedCornerShape(8)),
                contentScale = ContentScale.Crop,
            )
            EvText(
                resource = title,
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimen.small)
                    .weight(1f)
            )
        }
    }
}

const val BY_DEV_ID = "byDevId"
const val PHOTO_ID = "photoId"
const val LIST_ID = "listId"
const val TITLE_ID = "titleId"

@Preview(showSystemUi = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun MainScreenPreview() {
    MainScreen({})
}