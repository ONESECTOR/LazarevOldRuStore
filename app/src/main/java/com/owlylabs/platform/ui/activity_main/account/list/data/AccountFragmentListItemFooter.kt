package com.owlylabs.platform.ui.account.list.data

import com.owlylabs.platform.constants.AppLogicConstants

class AccountFragmentListItemFooter : AccountFragmentListItemAbstract() {
    override fun getItemType(): Int {
        return AppLogicConstants.AccountListHolderType.FOOTER_HOLDER.ordinal
    }
}