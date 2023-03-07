package com.owlylabs.platform.model.repository.remote.callbacks.structure.section

import androidx.annotation.Keep
import androidx.room.Ignore

@Keep
data class SectionCallback(
    val id_section: Int,
    val sectionType: String,
    val typeList: Int,
    val title: String,
    val subTitle: String,
    val backgroundColor: String,
    val backgroundImage: String,
    val coverRatioWtoH: Double,
    val estimateWidthPercent: Int,
    @Ignore val item_ids: String?,
    val maxWidthDP: Int,
    @Ignore val maxWidthPixel: Int,
    val showAdditionalAction: Int,
    val lineColor: String?
)