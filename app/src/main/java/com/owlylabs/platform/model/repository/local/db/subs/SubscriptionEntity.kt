package com.owlylabs.platform.model.repository.local.db.subs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.owlylabs.platform.constants.DBConstants

@Entity
data class SubscriptionEntity(
    @PrimaryKey val product_id : String,
    val expiryTimeMillis : String
)