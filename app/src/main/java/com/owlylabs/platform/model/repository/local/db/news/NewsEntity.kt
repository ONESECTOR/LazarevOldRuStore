package com.owlylabs.platform.model.repository.local.db.news

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewsEntity (
    @PrimaryKey val id: String,
    val button_title: String,
    val date: String,
    val description: String,
    val jump: String,
    val jump_type: String,
    val text: String,
    val title: String,
    val url_img: String,
    val url_large_img: String
)