package com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_cancel

data class Params(
    val method: String,
    val package_name: String,
    val purchase_token: String,
    val subscription_id: String
)