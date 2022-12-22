package com.example.evagnelyrics.ui.compose.screen.list

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.Resource
import com.example.evagnelyrics.core.dimen
import com.example.evagnelyrics.ui.fragment.ACTION_BACK
import com.example.evagnelyrics.ui.fragment.ACTION_NEXT
import com.example.evagnelyrics.ui.theme.component.EvKeyboardAction
import com.example.evagnelyrics.ui.theme.component.EvTextField
import com.example.evagnelyrics.ui.theme.component.EvTextStyle

@Composable
fun ListScreen(
    viewModel: ListViewModel = hiltViewModel(),
    navTo: (route: String) -> Unit = {},
) {

    val favMode by viewModel.favMode.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val searchMode by viewModel.searchMode.collectAsState(false)
    val text by viewModel.searchField.observeAsState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = {
                    SlideFromRight(visible = !searchMode) {
                        Text(
                            text = stringResource(id = R.string.songs),
                            color = Color.White
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
                            //reset the list
                            viewModel.searching("")
                        } else {
                            navTo(ACTION_BACK)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "back arrow",
                            tint = Color.White
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
                                    tint = if (favMode) Color.Red else Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(onClick = {
                                viewModel.toggleSearchMode()
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "search",
                                    tint = Color.White
                                )
                            }

                        }
                    }
                },
            )
        }
    ) {
        val titles by viewModel.titles.collectAsState(emptyList())
        SongsList(Modifier.padding(it), titles, navTo)

        //collect uiStates
        val uiState by viewModel.uiState.collectAsState(Resource.Success(Unit))

        LaunchedEffect(key1 = true) {
            when (val state = uiState) {
                is Resource.Error -> {
                    scaffoldState.snackbarHostState.showSnackbar(state.msg)
                }
                is Resource.Success -> {}
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
            SlideFromRight(visibleState = visibleState) {
                SongItem(
                    Modifier.clickable {
                        navTo(ACTION_NEXT + "/" + songs[index])
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
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.dimen.verySmall,
                start = MaterialTheme.dimen.superSmall,
                end = MaterialTheme.dimen.superSmall
            )
            .height(MaterialTheme.dimen.almostGiant)
            .background(MaterialTheme.colors.primary, shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Icon(
                imageVector = Icons.Rounded.PlaylistPlay,
                contentDescription = null,
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Text(text = title, color = Color.White)
        }
        //collect favs list
        val favTitles by viewModel.favs.collectAsState(emptyList())
        FavIcon(title, favTitles) { viewModel.favAction(it) }
    }
}

@Composable
fun FavIcon(title: String, favs: List<String> = emptyList(), favAction: (String) -> Unit) {
    IconButton(
        modifier = Modifier.padding(end = MaterialTheme.dimen.mediumSmall),
        onClick = { favAction(title) }) {
        Icon(
            imageVector = Icons.Rounded.Favorite,
            contentDescription = "fav button",
            tint = if (favs.contains(title)) Color.Red else Color.White
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