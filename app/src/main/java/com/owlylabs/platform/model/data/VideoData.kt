package com.owlylabs.platform.model.data

import androidx.room.Entity
import androidx.room.PrimaryKey

data class VideoData (
    val id_book: String,
    val book_type: String,
    val description: String,
    val in_app_p_key_video: String,
    val name_book: String,
    val url_img: String,
    val url_large_img: String,
    val url_video: String,
    val video_duration: String,
    val video_size: String
): Comparable<VideoData>{
    override fun compareTo(other: VideoData): Int {
        if (this.id_book != other.id_book) return -1
        if (this.book_type != other.book_type) return -1
        if (this.description != other.description) return -1
        if (this.in_app_p_key_video != other.in_app_p_key_video) return -1
        if (this.name_book != other.name_book) return -1
        if (this.url_img != other.url_img) return -1
        if (this.url_large_img != other.url_large_img) return -1
        if (this.url_video != other.url_video) return -1
        if (this.video_duration != other.video_duration) return -1
        if (this.video_size != other.video_size) return -1
        return 0
    }

}