package com.example.evagnelyrics.ui.screen.picture

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.MainViewModel
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextStyle
import com.example.evagnelyrics.ui.util.SlideInOutFrom
import com.example.evagnelyrics.ui.util.Vertically
import com.example.evagnelyrics.ui.util.Where
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PictureScreen(
    page: Int = 0,
    navController: NavHostController = LocalNavController.current!!,
    mainViewModel: MainViewModel = hiltViewModel()
) {

    val visibleState = MutableTransitionState(false).apply {
        targetState = true
    }
    val myBox by mainViewModel.myBoxState.collectAsState()

    SlideInOutFrom(where = Where.Vertical(Vertically.Bottom), visibleState = visibleState) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbar, pager) = createRefs()
            TopAppBar(
                title = {
                    EvText(
                        resource = R.string.picture_title,
                        style = EvTextStyle.Head,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
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

}

@Preview(showSystemUi = true)
@Composable
fun PictureScreenPreview() {
    PictureScreen()
}