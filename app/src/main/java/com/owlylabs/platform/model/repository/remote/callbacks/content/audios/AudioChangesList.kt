package com.owlylabs.platform.model.repository.remote.callbacks.content.audios

import androidx.annotation.Keep

@Keep
data class AudioChangesList (
    var added : ArrayList<AudioBookCallback>?,
    var edition : ArrayList<AudioBookCallback>?,
    var deleted : ArrayList<AudioBookCallback>?
)