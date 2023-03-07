package com.owlylabs.platform.model.repository.local.db.videos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoEntity (
    @PrimaryKey val id_book: String,
    val book_type: String,
    val description: String,
    val in_app_p_key_video: String,
    val name_book: String,
    val url_img: String,
    val url_large_img: String,
    val url_video: String,
    val video_duration: String,
    val video_size: String
)