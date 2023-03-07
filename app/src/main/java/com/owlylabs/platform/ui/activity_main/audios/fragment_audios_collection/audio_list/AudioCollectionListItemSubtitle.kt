package com.owlylabs.platform.ui.audios.fragment_audios_collection.audio_list

import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class AudioCollectionListItemSubtitle(data: SectionData) : AudioCollectionListItemAbstract(data) {
    var data: SectionData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.BookCollectionViewHolderType.SUBTITLE.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.id_section
    }

    override fun getItemData(): Any {
        return this
    }

    override fun updateData(newData: Any) {
        if (newData is SectionData){
            data = newData
        }
    }
}