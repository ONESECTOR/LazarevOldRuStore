package com.owlylabs.platform.model.repository.local.db.table_relations

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Section_Item (
    @PrimaryKey (autoGenerate = true)
    val id : Long,
    val sectionId : Int,
    val itemId : Int
)