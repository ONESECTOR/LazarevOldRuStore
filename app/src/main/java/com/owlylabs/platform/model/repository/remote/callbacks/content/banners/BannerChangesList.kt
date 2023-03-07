package com.owlylabs.platform.model.repository.remote.callbacks.content.banners

import androidx.annotation.Keep

@Keep
data class BannerChangesList (
    var added : ArrayList<BannerCallback>?,
    var edition : ArrayList<BannerCallback>?,
    var deleted : ArrayList<BannerCallback>?
)