package com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract

import com.owlylabs.platform.model.data.SectionData

abstract class BooksCollectionListItemAbstract(val sectionData: SectionData) : Comparable<BooksCollectionListItemAbstract> {
    abstract fun getItemType(): Int
    abstract fun getInnerDataId(): Int
    abstract fun getItemData(): Any
    open fun updateData(newData: Any){}

    override fun compareTo(other: BooksCollectionListItemAbstract): Int {
        if (this.getItemType() != other.getItemType()) return -1
        if (this.getInnerDataId() != other.getInnerDataId()) return -1
        return 0
    }
}