package com.owlylabs.platform.model.repository.local.db.udid

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UdidEntity (
    @PrimaryKey var udid: String
)