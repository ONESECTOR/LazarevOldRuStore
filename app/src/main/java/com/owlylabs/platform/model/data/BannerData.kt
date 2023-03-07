package com.owlylabs.platform.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class BannerData (
    val id: String,
    val button_title: String,
    val description: String,
    val jump: String,
    val jump_to_news: String,
    val jump_type: String,
    val sort: String,
    val title: String,
    val url_img: String
)