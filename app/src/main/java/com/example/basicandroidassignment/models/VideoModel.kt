package com.example.basicandroidassignment.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoModel(
    val id: Int,
    val title: String,
    val description: String,
    @SerialName(value="video_url")
    val videoUrl: String
)