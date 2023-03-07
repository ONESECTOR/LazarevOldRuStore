package com.owlylabs.platform.model.repository.local.db.content_timestamp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContentTimestampEntity (
    @PrimaryKey @ColumnInfo(defaultValue = "0") val lastTimestamp: Long
)