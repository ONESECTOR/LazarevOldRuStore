package com.owlylabs.platform.model.repository.local.db.account

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccountEntity (
    @PrimaryKey var name: String
)