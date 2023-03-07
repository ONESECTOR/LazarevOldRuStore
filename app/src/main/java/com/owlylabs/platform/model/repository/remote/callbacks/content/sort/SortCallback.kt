package com.owlylabs.platform.model.repository.remote.callbacks.content.sort

import androidx.annotation.Keep

@Keep
data class SortCallback(
    val id_book: String,
    val sort: String
)