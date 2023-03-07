package com.owlylabs.platform.model.repository.remote.callbacks.content.news

import androidx.annotation.Keep

@Keep
data class NewsChangesList (
    var added : ArrayList<NewsCallback>?,
    var edition : ArrayList<NewsCallback>?,
    var deleted : ArrayList<NewsCallback>?
)