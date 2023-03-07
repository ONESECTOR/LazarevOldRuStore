package com.owlylabs.platform.model.repository.remote.callbacks.recommended

import androidx.annotation.Keep
import androidx.room.PrimaryKey

@Keep
data class RecommendedBanner(
    val id: String,
    val transition_app: String,
    val url_android: String,
    val url_large_img: String,
    val url_link: String
)