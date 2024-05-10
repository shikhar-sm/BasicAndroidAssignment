package com.example.basicandroidassignment

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.basicandroidassignment.models.VideoModel
import com.example.basicandroidassignment.ui.MainViewModel
import com.example.basicandroidassignment.ui.theme.BasicAndroidAssignmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicAndroidAssignmentTheme(
                darkTheme = false
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val uiState: MainViewModel = viewModel()
                    uiState.MyApp(
                        Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun MainViewModel.MyApp(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Search(this@MyApp::search, modifier = modifier)
        LazyColumn(
            contentPadding = PaddingValues(8.dp)
        ) {
            items(videoList.value, { it.id }) {
                VideoDetails(video = it)
            }
        }
    }
}

@Composable
fun Search(onSearch: (String) -> Unit, modifier: Modifier) {
    var query by remember { mutableStateOf("") }
    TextField(
        value = query,
        onValueChange = { query = it },
        singleLine = true,
        label = { Text(text = stringResource(R.string.search)) },
        leadingIcon = {
            IconButton(onClick = { onSearch(query) }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = { query = "" }) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = stringResource(R.string.clear)
                )
            }
        },
        keyboardActions = KeyboardActions(onSearch = {onSearch(query)}),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
    )
}

@Composable
fun VideoDetails(video: VideoModel, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(0.dp),
    ) {
        VideoPlay(uri = video.videoUrl)
        Column(
            modifier = modifier.padding(8.dp)
        ) {
            Text(text = video.title, style = MaterialTheme.typography.titleSmall)
            Text(text = video.description, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun VideoPlay(uri: String) {
    val context = LocalContext.current
    val pWR by remember { mutableStateOf(false) }
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = ExoPlayer.REPEAT_MODE_ALL
            playWhenReady = pWR
            prepare()
            play()
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    AndroidView(
        factory = {
            PlayerView(context).apply {
                player = exoPlayer
                useController = true
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams
                        .MATCH_PARENT,
                    ViewGroup.LayoutParams
                        .MATCH_PARENT
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}
