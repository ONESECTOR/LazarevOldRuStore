package com.owlylabs.platform.model.data


data class NewsData(
    val id: String,
    val button_title: String,
    val date: String,
    val description: String,
    val jump: String,
    val jump_type: String,
    val text: String,
    val title: String,
    val url_img: String,
    val url_large_img: String
) : Comparable<NewsData> {
    override fun compareTo(other: NewsData): Int {
        if (this.id != other.id) return -1
        if (this.button_title != other.button_title) return -1
        if (this.date != other.date) return -1
        if (this.description != other.description) return -1
        if (this.jump != other.jump) return -1
        if (this.jump_type != other.jump_type) return -1
        if (this.text != other.text) return -1
        if (this.title != other.title) return -1
        if (this.url_img != other.url_img) return -1
        if (this.url_large_img != other.url_large_img) return -1
        return 0
    }
}