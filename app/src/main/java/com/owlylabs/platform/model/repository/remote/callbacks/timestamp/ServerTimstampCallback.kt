package com.owlylabs.platform.model.repository.remote.callbacks.timestamp

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
data class ServerTimstampCallback (
    val timestamp: Long
)