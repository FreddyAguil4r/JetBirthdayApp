package com.opbengalas.birthdayapp.screens.VideoFeedScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.opbengalas.birthdayapp.screens.VideoFeedScreen.VideoFeedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoFeedTopBar(
    navController: NavController,
    videoFeedViewModel: VideoFeedViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Video",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.primary,
            )
        )
    }
}