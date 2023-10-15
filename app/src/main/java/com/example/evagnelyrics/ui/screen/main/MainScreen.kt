package com.example.evagnelyrics.ui.screen.main

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ModeNight
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.domain.model.SystemMode
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    navController: NavHostController = LocalNavController.current!!,
) {
    val viewModel = koinViewModel<MainVM>()
    val darkMode by viewModel.darkMode.collectAsState(initial = SystemMode.Dark)

    MainScreen(
        onSwitchDarkMode = viewModel::switchDarkMode,
        darkMode = darkMode
    ) { route -> navController.navigate(route) }


}

@Composable
private fun MainScreen(
    darkMode: SystemMode,
    onSwitchDarkMode: () -> Unit,
    onNavigate: (route: String) -> Unit,
) {
    val smallDimen = MaterialTheme.dimen.small
    val mediumDimen = MaterialTheme.dimen.medium

    val constraints = ConstraintSet {
        val byDevRef = createRefFor(BY_DEV_ID)
        val photoRef = createRefFor(PHOTO_ID)
        val listRef = createRefFor(LIST_ID)
        val titleRef = createRefFor(TITLE_ID)
        val darkModeRef = createRefFor(DARK_MODE_SWITCHER_ID)
        val topGuideLine35 = createGuidelineFromTop(0.35f)
        val topGuideLine45 = createGuidelineFromTop(0.45f)
        val topGuideLine90 = createGuidelineFromTop(0.90f)

        constrain(darkModeRef) {
            top.linkTo(parent.top, margin = smallDimen)
            end.linkTo(parent.end, margin = smallDimen)
        }

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
        DarkModeIconButton(
            darkMode = darkMode,
            onSwitchDarkMode = onSwitchDarkMode,
            modifier = Modifier.layoutId(DARK_MODE_SWITCHER_ID)
        )

        MainImage(Modifier.layoutId(PHOTO_ID))

        Text(
            text = stringResource(id = R.string.app_name),
            modifier = Modifier.layoutId(TITLE_ID).testTag("app title"),
            style = MaterialTheme.typography.headlineMedium
        )

        MainCards(onNavigate = onNavigate, modifier = Modifier.layoutId(LIST_ID))

        Text(
            text = stringResource(id = R.string.by_dev),
            modifier = Modifier
                .padding(MaterialTheme.dimen.large)
                .layoutId(BY_DEV_ID),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun MainCards(onNavigate: (route: String) -> Unit, modifier: Modifier = Modifier) {
    LazyRow(
        modifier = modifier
            .padding(end = MaterialTheme.dimen.verySmall),
    ) {
        val pictures = listOf(R.drawable.img2_7190, R.drawable.img4_7236)
        val titles = listOf(R.string.songs, R.string.title_wallpapers)
        val destinations = listOf(Route.List.toString(), Route.Gallery.toString())
        items(count = 2) { index ->
            MainCard(image = pictures[index], title = stringResource(titles[index])) {
                onNavigate(destinations[index])
            }
        }
    }
}

@Composable
private fun MainImage(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.img3_7228_crop),
        contentDescription = "front",
        modifier = modifier
            .aspectRatio(1f)
            .border(
                MaterialTheme.dimen.verySmall,
                MaterialTheme.colorScheme.secondary,
                RoundedCornerShape(50)
            )
            .clip(RoundedCornerShape(50)),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun DarkModeIconButton(
    darkMode: SystemMode,
    onSwitchDarkMode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedIconButton(
        onClick = onSwitchDarkMode,
        modifier = modifier
    ) {
        AnimatedContent(
            targetState = darkMode,
            transitionSpec = {
                slideInVertically(spring(Spring.DampingRatioHighBouncy)) { it } togetherWith
                        fadeOut() + slideOutVertically { it }
            }, label = "dark mode"
        ) {
            if (it == SystemMode.Light) {
                Icon(
                    imageVector = Icons.Rounded.ModeNight,
                    contentDescription = "night icon",
                )
            } else {
                Icon(
                    imageVector = Icons.Rounded.WbSunny,
                    contentDescription = "light icon",
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCard(
    @DrawableRes image: Int,
    title: String,
    navigate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(0.5625f)
            .padding(all = MaterialTheme.dimen.small),
        onClick = navigate
    ) {
        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = image),
                contentDescription = "$title image",
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clip(RoundedCornerShape(8)),
                contentScale = ContentScale.Crop,
            )
            Text(
                text = title,
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimen.small),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

const val BY_DEV_ID = "byDevId"
const val PHOTO_ID = "photoId"
const val LIST_ID = "listId"
const val TITLE_ID = "titleId"
const val DARK_MODE_SWITCHER_ID = "dark_mode_switcher"

@Preview
@Composable
fun MainScreenPreview() {
    EvagneLyricsTheme(darkTheme = true) {
        MainScreen(SystemMode.Dark, {}) {}
    }
}