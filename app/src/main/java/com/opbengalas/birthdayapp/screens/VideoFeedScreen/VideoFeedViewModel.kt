package com.opbengalas.birthdayapp.screens.VideoFeedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opbengalas.birthdayapp.models.VideoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoFeedViewModel @Inject constructor() : ViewModel() {
    private val _videoList = MutableStateFlow<List<VideoItem>>(emptyList())
    val videoList: StateFlow<List<VideoItem>> = _videoList

    init {
        viewModelScope.launch {
            _videoList.value = listOf(
                VideoItem(
                    1,
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                ),
                VideoItem(
                    2,
                    "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4"
                )
            )
        }
    }
}