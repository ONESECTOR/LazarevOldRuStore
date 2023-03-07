package com.owlylabs.platform.model.data.tab_list_item_types.audio

import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemAbstract
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemSingleAudio(sectionData: SectionData, data: AudioData) : TabListItemAbstract(sectionData) {
    var data: AudioData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.AUDIO.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.idBook.toInt()
    }

    override fun getItemData(): Any {
        return this
    }
}