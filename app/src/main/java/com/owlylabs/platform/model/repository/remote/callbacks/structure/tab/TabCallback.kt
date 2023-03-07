package com.owlylabs.platform.model.repository.remote.callbacks.structure.tab

import androidx.annotation.Keep
import androidx.room.Ignore
import com.owlylabs.platform.model.repository.remote.callbacks.structure.section.SectionCallback

@Keep
data class TabCallback(
    val activePlatform: Int,
    val itemId: Int,
    @Ignore
    val sections: List<SectionCallback>,
    val sort: Int,
    val tabImageAndroid: String?,
    val tabTitle: String,
    val title: String,
    val tabType: String
)