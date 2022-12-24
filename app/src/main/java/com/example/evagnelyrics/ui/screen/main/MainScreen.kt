package com.example.evagnelyrics.ui.screen.main

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextStyle

@Composable
fun MainScreen(
    navController: NavHostController = LocalNavController.current!!
) {

    val smallDimen = MaterialTheme.dimen.small
    val mediumDimen = MaterialTheme.dimen.medium

    val constraints = ConstraintSet {
        val byDevRef = createRefFor(BY_DEV_ID)
        val photoRef = createRefFor(PHOTO_ID)
        val listRef = createRefFor(LIST_ID)
        val titleRef = createRefFor(TITLE_ID)
        val topGuideLine35 = createGuidelineFromTop(0.35f)
        val topGuideLine45 = createGuidelineFromTop(0.45f)

        constrain(photoRef) {
            top.linkTo(parent.top)
            bottom.linkTo(topGuideLine35)
            start.linkTo(parent.start, margin = mediumDimen)
            end.linkTo(parent.end, margin = mediumDimen)
        }

        constrain(titleRef) {
            top.linkTo(photoRef.bottom)
            bottom.linkTo(listRef.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }

        constrain(listRef) {
            top.linkTo(topGuideLine45, margin = smallDimen)
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
        Image(
            painter = painterResource(R.drawable.img3_7228_crop),
            contentDescription = "front",
            modifier = Modifier
                .width(280.dp)
                .aspectRatio(1f)
                .border(
                    MaterialTheme.dimen.verySmall,
                    MaterialTheme.colors.primaryVariant,
                    RoundedCornerShape(50)
                )
                .clip(RoundedCornerShape(50))
                .layoutId(PHOTO_ID),
            contentScale = ContentScale.Crop
        )

        EvText(
            resource = R.string.app_name,
            style = EvTextStyle.Head,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier.layoutId(TITLE_ID)
        )

        LazyRow(
            modifier = Modifier
                .height(320.dp)
                .padding(end = MaterialTheme.dimen.verySmall)
                .layoutId(LIST_ID)
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

        EvText(
            resource = R.string.by_dev,
            color = MaterialTheme.colors.primaryVariant,
            modifier = Modifier
                .layoutId(BY_DEV_ID)
                .padding(MaterialTheme.dimen.large)
        )
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
            .width(180.dp)
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
    MainScreen()
}