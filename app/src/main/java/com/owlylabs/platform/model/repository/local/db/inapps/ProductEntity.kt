package com.owlylabs.platform.model.repository.local.db.inapps

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProductEntity(
    @PrimaryKey val product_id : String,
    val has_access : Int
)