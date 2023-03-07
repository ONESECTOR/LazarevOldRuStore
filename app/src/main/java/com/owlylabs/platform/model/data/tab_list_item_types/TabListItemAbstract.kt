package com.owlylabs.platform.model.data.tab_list_item_types

import com.owlylabs.platform.model.data.SectionData

abstract class TabListItemAbstract(val sectionData: SectionData) {
    abstract fun getItemType(): Int
    abstract fun getInnerDataId(): Int
    abstract fun getItemData(): Any
    open fun updateData(newData: Any){}
}