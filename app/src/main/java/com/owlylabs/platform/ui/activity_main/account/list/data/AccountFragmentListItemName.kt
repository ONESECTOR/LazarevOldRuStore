package com.owlylabs.platform.ui.account.list.data

import com.owlylabs.platform.constants.AppLogicConstants

class AccountFragmentListItemName(val name: String) : AccountFragmentListItemAbstract() {
    override fun getItemType(): Int {
        return AppLogicConstants.AccountListHolderType.NAME_HOLDER.ordinal
    }
}