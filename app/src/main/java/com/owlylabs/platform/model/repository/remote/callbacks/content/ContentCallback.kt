package com.owlylabs.platform.model.repository.remote.callbacks.content

import androidx.annotation.Keep
import com.owlylabs.platform.model.repository.remote.callbacks.content.audios.AudioChangesList
import com.owlylabs.platform.model.repository.remote.callbacks.content.banners.BannerChangesList
import com.owlylabs.platform.model.repository.remote.callbacks.content.books.BookChangesList
import com.owlylabs.platform.model.repository.remote.callbacks.content.news.NewsChangesList
import com.owlylabs.platform.model.repository.remote.callbacks.content.sort.SortCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.videos.VideoChangesList

@Keep
data class ContentCallback(
    val timestamp: Long,
    val books: BookChangesList?,
    val audio: AudioChangesList?,
    val video: VideoChangesList?,
    val banners: BannerChangesList?,
    val news: NewsChangesList?,
    val sort: ArrayList<SortCallback>?
)