package com.owlylabs.platform.ui.activity_start_screen.subscription.list.data

import com.owlylabs.platform.constants.AppLogicConstants

class StartScreenSubscriptionRecyclerViewItemHeader : StartScreenSubscriptionRecyclerViewItemAbstract() {

    override fun getItemType(): Int {
        return AppLogicConstants.StartScreenSubscriptionHolderType.HEADER_HOLDER.ordinal
    }
}