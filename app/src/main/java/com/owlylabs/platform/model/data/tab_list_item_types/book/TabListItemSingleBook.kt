package com.owlylabs.platform.model.data.tab_list_item_types.book

import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemAbstract
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemSingleBook(sectionData: SectionData, data: BookData) : TabListItemAbstract(sectionData) {
    var data: BookData
    var progress = 0

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.BOOK.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.idBook.toInt()
    }

    override fun getItemData(): Any {
        return this
    }

    override fun updateData(newData: Any) {
        if (newData is BookData){
            data = newData
        }
    }
}