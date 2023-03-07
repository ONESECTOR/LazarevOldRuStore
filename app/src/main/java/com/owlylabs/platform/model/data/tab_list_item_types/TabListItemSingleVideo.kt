package com.owlylabs.platform.model.data.tab_list_item_types

import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.data.VideoData
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemSingleVideo(sectionData: SectionData, data: VideoData) : TabListItemAbstract(sectionData) {
    var data: VideoData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.VIDEO.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.id_book.toInt()
    }

    override fun getItemData(): Any {
        return this
    }
}