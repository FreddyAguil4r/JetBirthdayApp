package com.opbengalas.birthdayapp.screens.VideoFeedScreen

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.opbengalas.birthdayapp.models.VideoItem

@Composable
fun VideoFeedScreen(
    navController: NavController,
    videoFeedViewModel: VideoFeedViewModel
) {
    val videos = videoFeedViewModel.videoList.collectAsState(initial = emptyList()).value
    Log.d("VideoFeedScreen", "Número de videos cargados: ${videos.size}")

    val pagerState = rememberPagerState(pageCount = { videos.size })

    Box(modifier = Modifier.fillMaxSize()) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page < videos.size) {
                val videoItem = videos[page]
                Log.d(
                    "VideoFeedScreen",
                    "Mostrando página: $page, Video URL: ${videoItem.videoUrl}"
                )
                VideoPlayer(videoItem = videoItem)
            }
        }
    }
}

@Composable
fun VideoPlayer(videoItem: VideoItem) {
    Log.d("VideoPlayer", "Inicializando VideoPlayer para URL: ${videoItem.videoUrl}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        val context = LocalContext.current

        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                videoItem.videoUrl?.let { url ->
                    Log.d("VideoPlayer", "Cargando video desde URL: $url")
                    setMediaItem(MediaItem.fromUri(Uri.parse(url)))
                    prepare()
                    playWhenReady = true
                } ?: Log.w("VideoPlayer", "URL de video es nula")
            }
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            update = {
                it.player = exoPlayer
            }
        )

        if (videoItem.videoUrl == null) {
            Log.w("VideoPlayer", "Mostrando indicador de progreso, video aún no cargado")
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(50.dp)
            )
        }

        if (videoItem.isAd) {
            Log.d("VideoPlayer", "Este video es un anuncio")
            Text(
                text = "Ad",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
            )
        }
    }
}
