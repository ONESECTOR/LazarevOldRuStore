package com.owlylabs.platform.ui.activity_start_screen.subscription.list.data

import com.owlylabs.platform.constants.AppLogicConstants

class StartScreenSubscriptionRecyclerViewItemFooter() : StartScreenSubscriptionRecyclerViewItemAbstract(){
    override fun getItemType(): Int {
        return AppLogicConstants.StartScreenSubscriptionHolderType.FOOTER_HOLDER.ordinal
    }
}