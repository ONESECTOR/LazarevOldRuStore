package com.owlylabs.platform.model.data.tab_list_item_types.audio

import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemAbstract
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemHorizontalAudioList(sectionData: SectionData, audioList: List<AudioData>) : TabListItemAbstract(sectionData) {
    var audioList: List<AudioData>

    init {
        this.audioList = audioList
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.AUDIO_HORIZONTAL_SECTION.ordinal
    }

    override fun getInnerDataId(): Int {
        return sectionData.id_section
    }

    override fun getItemData(): Any {
        return this
    }


}