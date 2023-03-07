package com.owlylabs.platform.model.repository.local.db.recommended

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecommendedBannerEntity(
    @PrimaryKey
    val id: String,
    val transition_app: String,
    val url_android: String,
    val url_large_img: String,
    val url_link: String
)