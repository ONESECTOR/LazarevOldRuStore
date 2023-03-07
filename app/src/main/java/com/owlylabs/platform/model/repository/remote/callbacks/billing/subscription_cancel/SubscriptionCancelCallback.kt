package com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_cancel

data class SubscriptionCancelCallback(
    val params: Params,
    val result: String,
    val status: String
)