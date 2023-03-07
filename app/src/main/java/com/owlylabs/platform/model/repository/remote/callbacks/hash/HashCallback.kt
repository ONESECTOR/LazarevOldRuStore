package com.owlylabs.platform.model.repository.remote.callbacks.hash

import androidx.annotation.Keep
import androidx.room.Ignore

@Keep
data class HashCallback(
    val hash: String,
    @Ignore
    val status: String
)