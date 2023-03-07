package com.owlylabs.platform.model.repository.local.db.table_relations

import androidx.room.Entity

@Entity (primaryKeys = arrayOf("tabId", "sectionId"))
data class Tab_Section (
    val tabId : Int,
    val sectionId : Int
)