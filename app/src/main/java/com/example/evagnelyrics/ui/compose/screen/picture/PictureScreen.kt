package com.example.evagnelyrics.ui.compose.screen.picture

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.fragment.ACTION_BACK
import com.example.evagnelyrics.ui.theme.EvagneLyricsTheme
import com.example.evagnelyrics.ui.theme.component.EvText
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

const val TOP_APP_BAR_ID = "top-app-id"
const val VIEW_PAGER_ID = "view-pager-id"

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PictureScreen(
    page: Int,
    navTo: (String) -> Unit
) {

    EvagneLyricsTheme {
        val constraints = ConstraintSet {
            val appBarRef = createRefFor(TOP_APP_BAR_ID)
            val viewPagerRef = createRefFor(VIEW_PAGER_ID)

            constrain(appBarRef) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }

            constrain(viewPagerRef) {
                top.linkTo(appBarRef.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
        }
        ConstraintLayout(constraintSet = constraints, modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { EvText(resource = R.string.picture_title) },
                navigationIcon = {
                    IconButton(onClick = { navTo(ACTION_BACK) }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back arrow"
                        )
                    }
                },
                modifier = Modifier.layoutId(TOP_APP_BAR_ID)
            )
            val pagerState = rememberPagerState(initialPage = page)
            HorizontalPager(
                count = Items.images.size,
                state = pagerState,
                itemSpacing = MaterialTheme.dimen.verySmall
            ) { index ->
                Image(
                    painter = painterResource(Items.images[index]),
                    contentDescription = "image $index",
                    modifier = Modifier.layoutId(VIEW_PAGER_ID),
                    contentScale = ContentScale.FillWidth
                )
            }
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun PictureScreenPreview() {
    PictureScreen(1) {}
}