package com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract

import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class BooksCollectionListItemSingleBook(sectionData: SectionData, data: BookData) : BooksCollectionListItemAbstract(sectionData) {
    var data: BookData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.BookCollectionViewHolderType.BOOK.ordinal
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