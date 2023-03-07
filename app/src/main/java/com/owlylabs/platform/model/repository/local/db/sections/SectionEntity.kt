package com.owlylabs.platform.model.repository.local.db.sections

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SectionEntity(
    @PrimaryKey val id_section: Int,
    val sectionType : String,
    val typeList : Int?,
    val title : String?,
    val subTitle : String?,
    val backgroundColor : String?,
    val backgroundImage : String?,
    @ColumnInfo(typeAffinity = ColumnInfo.REAL)  val coverRatioWtoH : Double?,
    val estimateWidthPercent: Int,
    val maxWidthDP : Int?,
    val showAdditionalAction: Int?,
    val lineColor: String?
)