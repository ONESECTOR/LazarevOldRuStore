package com.owlylabs.platform.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class SectionData(
    val id_section: Int,
    val sectionType : String,
    val typeList : Int?,
    val title : String?,
    val subTitle : String?,
    val backgroundColor : String?,
    val backgroundImage : String?,
    val coverRatioWtoH : Double?,
    val estimateWidthPercent: Int,
    val maxWidthDP : Int?,
    val showAdditionalAction: Int?,
    val lineColor: String?
) : Comparable<SectionData> {
    override fun compareTo(other: SectionData): Int {
        if (this.id_section != other.id_section) return -1
        if (this.sectionType != other.sectionType) return -1
        if (this.typeList != other.typeList) return -1
        if (this.title != other.title) return -1
        if (this.subTitle != other.subTitle) return -1
        if (this.backgroundColor != other.backgroundColor) return -1
        if (this.backgroundImage != other.backgroundImage) return -1
        if (this.coverRatioWtoH != other.coverRatioWtoH) return -1
        if (this.estimateWidthPercent != other.estimateWidthPercent) return -1
        if (this.maxWidthDP != other.maxWidthDP) return -1
        if (this.showAdditionalAction != other.showAdditionalAction) return -1
        if (this.lineColor != other.lineColor) return -1
        return 0
    }
}