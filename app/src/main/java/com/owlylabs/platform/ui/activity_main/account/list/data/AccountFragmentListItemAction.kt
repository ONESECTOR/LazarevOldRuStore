package com.owlylabs.platform.ui.account.list.data

import com.owlylabs.platform.constants.AppLogicConstants

class AccountFragmentListItemAction(val actionName: String) : AccountFragmentListItemAbstract() {
    override fun getItemType(): Int {
        return AppLogicConstants.AccountListHolderType.ACTION_HOLDER.ordinal
    }
}