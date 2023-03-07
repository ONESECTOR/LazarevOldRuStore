package com.owlylabs.platform.model.repository.local.db.books

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BookEntity (
    @PrimaryKey val idBook: String,
    val nameBook: String,
    val description: String,
    val countPage: String,
    val urlImg: String,
    val urlLargeImg: String,
    val bookType: String,
    val inAppPKey: String
)