package com.owlylabs.platform.model.data.tab_list_item_types.book

import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemAbstract
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemHorizontalBookList(sectionData: SectionData, data: List<BookData>) : TabListItemAbstract(sectionData) {
    var data: List<BookData>

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.BOOK_HORIZONTAL_SECTION.ordinal
    }

    override fun getInnerDataId(): Int {
        return sectionData.id_section
    }

    override fun getItemData(): Any {
        return this
    }


}