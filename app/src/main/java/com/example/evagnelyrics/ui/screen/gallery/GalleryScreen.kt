package com.example.evagnelyrics.ui.screen.gallery

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Items
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.domain.model.MyBox
import com.example.evagnelyrics.ui.MainViewModel
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextStyle
import com.example.evagnelyrics.ui.util.Horizontally
import com.example.evagnelyrics.ui.util.ScaleFromCenter
import com.example.evagnelyrics.ui.util.SlideInOutFrom
import com.example.evagnelyrics.ui.util.Where
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun GalleryScreen(
    navController: NavHostController = LocalNavController.current!!,
    mainViewModel: MainViewModel = koinViewModel()
) {

    val onBack by remember {
        mutableStateOf(mainViewModel.previous() == Route.Picture)
    }

    val visibleState = MutableTransitionState(onBack).apply {
        if (!onBack) {
            targetState = true
        }
    }

    BackHandler(enabled = true) {
        mainViewModel.popScreen()
        navController.popBackStack()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                mainViewModel.pushScreen(Route.Gallery)
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    SlideInOutFrom(
        where = Where.Horizontal(Horizontally.End),
        visibleState = visibleState,
        durationMillis = 200
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    EvText(
                        resource = R.string.title_wallpapers,
                        style = EvTextStyle.Head,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        mainViewModel.popScreen()
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back on song"
                        )
                    }
                }
            )
            PicturesList(navController, 200, onBack)
        }
    }


}

@Composable
private fun PicturesList(
    navController: NavHostController,
    delay: Int,
    onBack: Boolean,
    mainViewModel: MainViewModel = koinViewModel()
) {

    val listVisibility = remember {
        mutableStateListOf<Boolean>()
    }.apply {
        addAll((1..5).map { false })
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxWidth(),
        columns = GridCells.Adaptive(MaterialTheme.dimen.giant * 3),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.small),
        contentPadding = PaddingValues(all = MaterialTheme.dimen.verySmall)
    ) {
        items(Items.images.size) { index ->
            ScaleFromCenter(
                visible = if (onBack) true else listVisibility[index],
                hasBounce = !onBack,
            ) {
                var myBox by remember {
                    mutableStateOf(MyBox())
                }

                Image(
                    painter = painterResource(
                        id = when (index) {
                            0 -> R.drawable.img1_7185
                            1 -> R.drawable.img2_7190
                            2 -> R.drawable.img3_7228
                            3 -> R.drawable.img4_7236
                            4 -> R.drawable.img5_7281
                            else -> R.drawable.img1_7185
                        }
                    ),
                    contentDescription = "image # $index",
                    modifier = Modifier
                        .aspectRatio(0.6625f)
                        .clip(RoundedCornerShape(5))
                        .clickable {
                            mainViewModel.setBox(myBox)
                            navController.navigate(Route.Picture.toString() + "/$index")
                        }
                        .onGloballyPositioned {
                            val rect = it.boundsInRoot()
                            myBox = MyBox(
                                topLeftX = rect.topLeft.x.roundToInt(),
                                topLeftY = rect.topLeft.y.roundToInt(),
                                width = rect.width.roundToInt(),
                                height = rect.height.roundToInt()
                            )
                        }
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        (0..4).forEach {
            delay(if (it == 0) 100 + delay.toLong() else 200)
            listVisibility[it] = true
        }
    }
}