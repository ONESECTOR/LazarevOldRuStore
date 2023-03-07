package com.owlylabs.platform.ui.audios.fragment_audios_collection.audio_list

import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class AudioCollectionListItemSingleAudio(sectionData: SectionData, data: AudioData) : AudioCollectionListItemAbstract(sectionData) {
    var data: AudioData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.BookCollectionViewHolderType.BOOK.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.idBook.toInt()
    }

    override fun getItemData(): Any {
        return this
    }

    override fun updateData(newData: Any) {
        if (newData is AudioData){
            data = newData
        }
    }
}