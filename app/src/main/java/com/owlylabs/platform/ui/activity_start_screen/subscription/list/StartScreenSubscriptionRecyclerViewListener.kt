package com.owlylabs.platform.ui.activity_start_screen.subscription.list

import com.android.billingclient.api.SkuDetails

interface StartScreenSubscriptionRecyclerViewListener {
    fun onClickRestartBilling()
    fun onClickSelectSubscription(skudetails: SkuDetails)
    fun onClickTermsOfUsage(url: String)
    fun onClickSupport()
    fun onClickSubscribe(skuDetails: SkuDetails)
    fun onClickBack()
}