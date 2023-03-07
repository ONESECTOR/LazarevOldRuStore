package com.owlylabs.platform.ui.activity_start_screen.subscription.list.data

import com.android.billingclient.api.SkuDetails
import com.owlylabs.platform.constants.AppLogicConstants

class StartScreenSubscriptionRecyclerViewItemSubscription(val skuDetail: SkuDetails) : StartScreenSubscriptionRecyclerViewItemAbstract(){

    var isSelected = false

    constructor(skuDetail: SkuDetails, isSelected : Boolean) : this(skuDetail){
        this.isSelected = isSelected
    }

    override fun getItemType(): Int {
        return AppLogicConstants.StartScreenSubscriptionHolderType.SUBSCRIPTION_HOLDER.ordinal
    }

}