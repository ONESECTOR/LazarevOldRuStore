package com.owlylabs.platform.model.repository.remote.callbacks.content.videos

import androidx.annotation.Keep

@Keep
data class VideoChangesList (
    var added : ArrayList<VideoCallback>?,
    var edition : ArrayList<VideoCallback>?,
    var deleted : ArrayList<VideoCallback>?
)