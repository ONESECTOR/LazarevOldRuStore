package com.owlylabs.platform.model.repository.remote.callbacks.udid

import androidx.annotation.Keep
import androidx.room.Ignore

@Keep
data class UdidCallback(
    @Ignore
    val status: String,
    val udid: String
)