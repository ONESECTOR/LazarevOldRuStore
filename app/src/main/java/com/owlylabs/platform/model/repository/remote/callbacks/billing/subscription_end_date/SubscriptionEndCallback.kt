package com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_end_date

import androidx.annotation.Keep

@Keep
data class SubscriptionEndCallback(
    val result: String,
    val status: String
)