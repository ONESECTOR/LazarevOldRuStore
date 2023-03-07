package com.owlylabs.platform.ui.account.list.data

import com.owlylabs.platform.model.data.RecommendedData
import com.owlylabs.platform.constants.AppLogicConstants

class AccountFragmentListItemBanner(val recommendedData: RecommendedData) : AccountFragmentListItemAbstract() {
    override fun getItemType(): Int {
        return AppLogicConstants.AccountListHolderType.BANNER_HOLDER.ordinal
    }
}