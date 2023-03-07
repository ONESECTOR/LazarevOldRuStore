package com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract

import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class BooksCollectionListItemSubtitle(data: SectionData) : BooksCollectionListItemAbstract(data) {
    var data: SectionData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.BookCollectionViewHolderType.SUBTITLE.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.id_section
    }

    override fun getItemData(): Any {
        return this
    }

    override fun updateData(newData: Any) {
        if (newData is SectionData){
            data = newData
        }
    }
}