package com.example.evagnelyrics.ui.screen.picture

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(
    page: Int = 0,
) {

    val navController: NavHostController = LocalNavController.current!!

    PictureScreen(navController::popBackStack, page)

}

@Composable
@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
private fun PictureScreen(onBack: ()->Unit, page: Int) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (toolbar, pager) = createRefs()
        TopAppBar(
            title = {
                Text(text = stringResource(id = R.string.picture_title))
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "back arrow"
                    )
                }
            },
            modifier = Modifier.constrainAs(toolbar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.wrapContent
            }
        )

        val pagerState = rememberPagerState(page)
        HorizontalPager(
            modifier = Modifier.constrainAs(pager) {
                top.linkTo(toolbar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            },
            count = Items.images.size,
            itemSpacing = MaterialTheme.dimen.verySmall,
            state = pagerState,
        ) { index ->
            Image(
                painter = painterResource(Items.images[index]),
                contentDescription = "image $index",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun PictureScreenPreview() {
    PictureScreen({}, 2)
}