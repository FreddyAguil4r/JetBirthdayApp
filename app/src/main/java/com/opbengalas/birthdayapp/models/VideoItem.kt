package com.opbengalas.birthdayapp.models

data class VideoItem(
    val id: Int,
    val videoUrl: String?,
    val isAd: Boolean = false
)