package com.owlylabs.platform.model.repository.remote.callbacks.billing.get_skus

data class InAppProduct(
    val packageName: String,
    val purchaseType: String,
    val sku: String,
    val status: String
)