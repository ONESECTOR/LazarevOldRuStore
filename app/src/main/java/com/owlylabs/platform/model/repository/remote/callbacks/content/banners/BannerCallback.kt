package com.owlylabs.platform.model.repository.remote.callbacks.content.banners

import androidx.annotation.Keep

@Keep
data class BannerCallback(
    val button_title: String,
    val description: String,
    val id: String,
    val jump: String,
    val jump_to_news: String,
    val jump_type: String,
    val sort: String,
    val title: String,
    val url_img: String
)