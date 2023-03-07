package com.owlylabs.platform.model.data

data class BookData(
    val idBook: String,
    val nameBook: String,
    val description: String,
    val countPage: String,
    val urlImg: String,
    val urlLargeImg: String,
    val bookType: String,
    val inAppPKey: String
): Comparable<BookData>{
    override fun compareTo(other: BookData): Int {
        if (this.idBook != other.idBook) return -1
        if (this.nameBook != other.nameBook) return -1
        if (this.description != other.description) return -1
        if (this.countPage != other.countPage) return -1
        if (this.urlImg != other.urlImg) return -1
        if (this.urlLargeImg != other.urlLargeImg) return -1
        if (this.bookType != other.bookType) return -1
        if (this.inAppPKey != other.inAppPKey) return -1
        return 0
    }

}