package com.owlylabs.platform.model.repository.remote.callbacks.recommended

import androidx.annotation.Keep

@Keep
data class RecommendedAppsResponse(
    val added: List<RecommendedBanner>?,
    val last_date: Int,
    val period: String,
    val random_sorting: String
)