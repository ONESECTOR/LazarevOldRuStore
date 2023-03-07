package com.owlylabs.platform.model.repository.remote.callbacks.content.books

import androidx.annotation.Keep

@Keep
data class BookChangesList (
    var added : ArrayList<BookCallback>?,
    var edition : ArrayList<BookCallback>?,
    var deleted : ArrayList<BookCallback>?
)