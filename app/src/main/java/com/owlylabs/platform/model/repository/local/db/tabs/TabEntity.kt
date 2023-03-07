package com.owlylabs.platform.model.repository.local.db.tabs

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.owlylabs.platform.constants.DBConstants

@Entity
data class TabEntity(
    @ColumnInfo (name = DBConstants.COLUMN_INFO_TAB_ITEM_ID)
    @PrimaryKey val itemId : Int,
    val sort : Int,
    @ColumnInfo (name = DBConstants.COLUMN_INFO_TAB_ITEM_ACTIVE_PLATFORM)
    val activePlatform : Int,
    val title : String,
    val tabTitle : String,
    val tabImageAndroid : String?,
    @ColumnInfo (name = DBConstants.COLUMN_INFO_TAB_ITEM_TAB_TYPE)
    val tabType: String
)