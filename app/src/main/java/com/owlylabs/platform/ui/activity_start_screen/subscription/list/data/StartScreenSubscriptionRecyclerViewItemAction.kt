package com.owlylabs.platform.ui.activity_start_screen.subscription.list.data

import com.owlylabs.platform.constants.AppLogicConstants

class StartScreenSubscriptionRecyclerViewItemAction(val text: String) : StartScreenSubscriptionRecyclerViewItemAbstract(){
    override fun getItemType(): Int {
        return AppLogicConstants.StartScreenSubscriptionHolderType.ACTION_HOLDER.ordinal
    }
}