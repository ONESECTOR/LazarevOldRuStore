package com.owlylabs.platform.model.data

data class TabData(
    val itemId: Int,
    val sort: Int,
    val activePlatform: Int,
    val title: String,
    val tabTitle: String,
    val tabImageAndroid: String?,
    val tabType : String
)