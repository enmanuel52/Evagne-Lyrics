package com.example.evagnelyrics.ui.screen.list

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PlaylistPlay
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.LocalNavController
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.component.EvKeyboardAction
import com.example.evagnelyrics.ui.theme.component.EvText
import com.example.evagnelyrics.ui.theme.component.EvTextField
import com.example.evagnelyrics.ui.theme.component.EvTextStyle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navController: NavHostController = LocalNavController.current!!
) {
    val scaffoldState = rememberScaffoldState()

    //viewModel states
    val favMode by viewModel.favMode.collectAsState()
    val searchMode by viewModel.searchMode.collectAsState(false)
    val text by viewModel.searchField.observeAsState()
    val titles by viewModel.titles.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    SlideFromRight(visible = !searchMode) {
                        EvText(
                            resource = R.string.songs,
                            style = EvTextStyle.Head,
                        )
                    }
                    SlideFromRight(visible = searchMode) {
                        EvTextField(
                            value = text.orEmpty(),
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = EvTextStyle.Head,
                            hint = R.string.search_song_hint,
                            keyboardAction = EvKeyboardAction.Go,
                        ) { viewModel.searching(it) }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (searchMode) {
                            viewModel.toggleSearchMode()
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back arrow",
                        )
                    }
                },
                actions = {
                    SlideFromRight(visible = !searchMode) {
                        Row {
                            IconButton(onClick = { viewModel.onFavMode() }) {
                                Icon(
                                    imageVector = Icons.Rounded.Favorite,
                                    contentDescription = "fav mode",
                                    tint = if (favMode) Color.Red else MaterialTheme.colors.onPrimary
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(onClick = {
                                viewModel.toggleSearchMode()
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "search",
                                )
                            }

                        }
                    }
                },
            )
        }
    ) {
        SongsList(Modifier.padding(it), titles) { title: String ->
            navController.navigate(Route.Song.toString() + "/$title")
        }

        //collect uiStates
        LaunchedEffect(key1 = true) {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is Resource.Error -> {
                        scaffoldState.snackbarHostState.showSnackbar(state.msg)
                    }
                    is Resource.Success -> {}
                }
            }
        }
    }
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<String> = emptyList(),
    navTo: (title: String) -> Unit = {},
) {
    LazyColumn {
        items(songs.size) { index ->
            val visibleState = MutableTransitionState(false).apply {
                targetState = true
            }
            SlideFromRight(
                visibleState = visibleState,
                delayMillis = index * 200 + 100,
                durationMillis = 250
            ) {
                SongItem(
                    Modifier.clickable {
                        navTo(songs[index])
                    },
                    title = songs[index]
                )
            }
        }
    }
}

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    title: String,
    viewModel: ListViewModel = hiltViewModel()
) {
    Card() {

    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.dimen.verySmall,
                start = MaterialTheme.dimen.superSmall,
                end = MaterialTheme.dimen.superSmall
            )
            .height(MaterialTheme.dimen.almostGiant)
            .clip(shape = RoundedCornerShape(20))
            .background(color = MaterialTheme.colors.primary, shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Icon(
                imageVector = Icons.Rounded.PlaylistPlay,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Text(text = title, color = MaterialTheme.colors.onPrimary)
        }
        //collect favs list
        val favTitles by viewModel.favorites.collectAsState(emptyList())
        FavIcon(title, favTitles.map { it.title }) { viewModel.favAction(it) }
    }
}

@Composable
fun FavIcon(title: String, favTitles: List<String> = emptyList(), favAction: (String) -> Unit) {
    IconButton(
        modifier = Modifier.padding(end = MaterialTheme.dimen.mediumSmall),
        onClick = { favAction(title) }) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "fav button",
            tint = if (favTitles.contains(title)) Color.Red else MaterialTheme.colors.onPrimary
        )
    }
}

/**
 * AnimatedVisibility for slide from right and hide on right*/
@Composable
fun SlideFromRight(
    visibleState: MutableTransitionState<Boolean>,
    delayMillis: Int = 0,
    durationMillis: Int = 400,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = slideInHorizontally(
            tween(
                durationMillis = 400,
                delayMillis = delayMillis
            )
        ) { it } + fadeIn(
            tween(
                durationMillis = 400, delayMillis = delayMillis
            )
        ),
        exit = slideOutHorizontally(
            tween(
                durationMillis = 400,
                delayMillis = delayMillis
            )
        ) { it } + fadeOut(
            tween(
                durationMillis = 400, delayMillis = delayMillis
            )
        ),
    ) {
        content()
    }
}

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
@Preview(showSystemUi = true)
fun ListScreenPreview() {
    ListScreen()
}

@Composable
@Preview(showSystemUi = true)
fun SongsListScreenPreview() {
    SongsList(songs = listOf("Hello"))
}