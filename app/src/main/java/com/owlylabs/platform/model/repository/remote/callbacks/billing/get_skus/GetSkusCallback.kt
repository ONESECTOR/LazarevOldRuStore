package com.owlylabs.platform.model.repository.remote.callbacks.billing.get_skus

data class GetSkusCallback(
    val inAppProducts: List<InAppProduct>,
    val status: String
)