package com.example.evagnelyrics.ui.screen.list

import android.media.MediaPlayer
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.example.evagnelyrics.R
import com.example.evagnelyrics.core.*
import com.example.evagnelyrics.ui.navigation.Route
import com.example.evagnelyrics.ui.theme.Amber500
import com.example.evagnelyrics.ui.theme.Grey800
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
    //composable
    val scaffoldState = rememberScaffoldState()

    //viewModel states
    val favMode by viewModel.favState
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
        Column(
            Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SongsList(
                modifier = Modifier,
                songs = titles,
            ) { title: String ->
                navController.navigate(Route.Song.toString() + "/$title")
            }

            DiscJockeyContainer(
                Modifier
                    .padding(bottom = MaterialTheme.dimen.lessLarge)
                    .size(120.dp)
            )
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
private fun DiscJockeyContainer(
    modifier: Modifier = Modifier,
    viewModel: ListViewModel = hiltViewModel(),
) {
    val playingSong by viewModel.playingSong
    val audioState by viewModel.audioState

    SlideFromLeft(
        visible = when (audioState) {
            Audio.Pause -> false
            Audio.Running -> true
        }, durationMillis = 1000
    ) {
        DiscJockey(
            playingSong,
            modifier = modifier
        )
    }
}

@Composable
fun DiscJockey(title: String, modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val degrees by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    val cover = getCover(title)
    Image(
        painter = painterResource(cover ?: R.drawable.img3_7228_crop),
        contentDescription = "cover image",
        modifier = modifier
            .clip(RoundedCornerShape(percent = 50))
            .border(
                MaterialTheme.dimen.verySmall,
                color = MaterialTheme.colors.primaryVariant,
                RoundedCornerShape(percent = 50)
            )
            .rotate(degrees),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun SongsList(
    modifier: Modifier = Modifier,
    songs: List<String> = emptyList(),
    navTo: (title: String) -> Unit = {},
) {
    LazyColumn(modifier) {
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
                    Modifier,
                    title = songs[index],
                ) {
                    navTo(it)
                }
            }
        }
    }
}

enum class Audio { Pause, Running }

@Composable
fun SongItem(
    modifier: Modifier = Modifier,
    title: String,
    viewModel: ListViewModel = hiltViewModel(),
    navTo: (title: String) -> Unit = {},
) {
    val context = LocalContext.current

    val mainAudio by viewModel.audioState

    var audioState by remember { mutableStateOf(Audio.Pause) }

    val mediaPlayer: MediaPlayer? by remember {
        mutableStateOf(
            MediaPlayer.create(context, getResourceSong(title)).apply {
                setOnCompletionListener {
                    audioState = Audio.Pause
                    viewModel.setAudioState(Audio.Pause)
                }
            }
        )
    }

    //To observe the lifecycle
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE) {
                audioState = Audio.Pause
                viewModel.setAudioState(Audio.Pause)
                mediaPlayer?.pause()
                mediaPlayer?.seekTo(0)
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                mediaPlayer?.release()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = MaterialTheme.dimen.verySmall,
                start = MaterialTheme.dimen.superSmall,
                end = MaterialTheme.dimen.superSmall
            )
            .height(MaterialTheme.dimen.almostGiant)
            .clip(shape = RoundedCornerShape(20))
            .background(color = MaterialTheme.colors.surface, shape = RoundedCornerShape(20))
            .clickable {
                navTo(title)
            }
    ) {
        SlideFromLeft(visible = audioState == Audio.Running, mediaPlayer?.duration ?: 1000) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colors.primaryVariantPrimary,
                        shape = RoundedCornerShape(20)
                    )
            )
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
            ) {
                Icon(
                    imageVector = when (audioState) {
                        Audio.Pause ->
                            when (mainAudio) {
                                Audio.Pause -> Icons.Rounded.PlayCircle
                                Audio.Running -> Icons.Rounded.StopCircle
                            }
                        Audio.Running -> Icons.Rounded.Pause
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(50))
                        .padding(
                            vertical = MaterialTheme.dimen.verySmall,
                            horizontal = MaterialTheme.dimen.superSmall
                        )
                        .clickable {
                            if (mainAudio == Audio.Pause) {
                                when (audioState) {
                                    Audio.Pause -> {
                                        audioState = Audio.Running

                                        mediaPlayer?.start()
                                        viewModel.setAudioState(Audio.Running, title = title)
                                    }
                                    Audio.Running -> {
                                        //if viewModel audio is off this is also off
                                    }
                                }
                            } else if (mainAudio == Audio.Running) {
                                when (audioState) {
                                    Audio.Pause -> {
                                        //there is another running
                                    }
                                    Audio.Running -> {
                                        audioState = Audio.Pause
                                        mediaPlayer?.pause()
                                        mediaPlayer?.seekTo(0)
                                        viewModel.setAudioState(Audio.Pause)
                                    }
                                }
                            }
                        }
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.mediumSmall))
            Text(text = title, color = MaterialTheme.colors.onPrimary)
        }

        //collect favs list
        val favTitles by viewModel.favoritesFlow.collectAsState(emptyList())
        FavIcon(
            title,
            favTitles.map { it.title },
            Modifier.align(Alignment.CenterEnd)
        ) { viewModel.favAction(it) }

    }
}

@Composable
fun FavIcon(
    title: String,
    favTitles: List<String> = emptyList(),
    modifier: Modifier,
    favAction: (String) -> Unit
) {
    IconButton(
        modifier = modifier.padding(end = MaterialTheme.dimen.mediumSmall),
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
fun SlideFromLeft(
    visible: Boolean,
    durationMillis: Int,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            tween(durationMillis)
        ) { -it }, //+ fadeIn(tween(durationMillis))
        exit = fadeOut(
            tween(200)
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

fun getResourceSong(title: String) =
    when {
        Items.songs.map { it.title }.contains(title) -> R.raw.demo_morat
        else -> R.raw.stereo_love
    }

fun getCover(title: String) =
    when (title) {
        "Falsedad" -> R.drawable.cover_falsedad
        "Tu perdón" -> R.drawable.cover_tu_perdon
        "Tú" -> R.drawable.cover_tu
        else -> null
    }