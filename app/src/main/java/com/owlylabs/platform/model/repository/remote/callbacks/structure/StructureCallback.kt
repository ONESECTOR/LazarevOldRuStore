package com.owlylabs.platform.model.repository.remote.callbacks.structure

import androidx.annotation.Keep
import com.owlylabs.platform.model.repository.remote.callbacks.structure.tab.TabCallback

@Keep
data class StructureCallback(
    val tabData: List<TabCallback>,
    val updated: String
)