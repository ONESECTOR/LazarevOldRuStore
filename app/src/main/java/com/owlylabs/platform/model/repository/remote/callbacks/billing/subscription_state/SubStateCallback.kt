package com.owlylabs.platform.model.repository.remote.callbacks.billing.subscription_state

import androidx.annotation.Keep

@Keep
data class SubStateCallback(
    val acknowledgementState: Int,
    val autoRenewing: Boolean,
    val autoResumeTimeMillis: Any,
    val cancelReason: Int,
    val countryCode: String,
    val developerPayload: String,
    val emailAddress: Any,
    val expiryTimeMillis: String,
    val familyName: Any,
    val givenName: Any,
    val kind: String,
    val linkedPurchaseToken: Any,
    val orderId: String,
    val package_name: String,
    val paymentState: Int,
    val priceAmountMicros: String,
    val priceCurrencyCode: String,
    val product_id: String,
    val profileId: Any,
    val profileName: Any,
    val purchaseType: Int,
    val purchase_token: String,
    val startTimeMillis: String,
    val userCancellationTimeMillis: Any
)