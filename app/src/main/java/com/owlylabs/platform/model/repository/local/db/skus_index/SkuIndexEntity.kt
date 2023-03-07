package com.owlylabs.platform.model.repository.local.db.skus_index

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SkuIndexEntity(
    @PrimaryKey
    val sku : String,
    val purchaseType : String
)