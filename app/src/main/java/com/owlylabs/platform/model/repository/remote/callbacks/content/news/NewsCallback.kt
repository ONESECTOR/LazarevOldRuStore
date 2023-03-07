package com.owlylabs.platform.model.repository.remote.callbacks.content.news

import androidx.annotation.Keep

@Keep
data class NewsCallback(
    val button_title: String,
    val date: String,
    val description: String,
    val id: String,
    val jump: String,
    val jump_type: String,
    val text: String,
    val title: String,
    val url_img: String,
    val url_large_img: String
)