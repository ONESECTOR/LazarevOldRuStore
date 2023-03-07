package com.owlylabs.platform.model.data.tab_list_item_types

import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemSeparator(sectionData: SectionData) : TabListItemAbstract(sectionData) {

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.SEPARATOR.ordinal
    }

    override fun getInnerDataId(): Int {
        return sectionData.id_section
    }

    override fun getItemData(): Any {
        return this
    }
}