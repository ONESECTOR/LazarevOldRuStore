package com.owlylabs.platform.model.repository.remote.callbacks.content.videos

import androidx.annotation.Keep

@Keep
data class VideoCallback(
    val book_type: String,
    val description: String,
    val id_book: String,
    val in_app_p_key_video: String,
    val name_book: String,
    val url_img: String,
    val url_large_img: String,
    val url_video: String,
    val video_duration: String,
    val video_size: String
)