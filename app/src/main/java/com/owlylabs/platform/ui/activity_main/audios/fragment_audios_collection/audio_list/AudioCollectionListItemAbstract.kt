package com.owlylabs.platform.ui.audios.fragment_audios_collection.audio_list

import com.owlylabs.platform.model.data.SectionData

abstract class AudioCollectionListItemAbstract(val sectionData: SectionData) : Comparable<AudioCollectionListItemAbstract> {
    abstract fun getItemType(): Int
    abstract fun getInnerDataId(): Int
    abstract fun getItemData(): Any
    open fun updateData(newData: Any){}

    override fun compareTo(other: AudioCollectionListItemAbstract): Int {
        if (this.getItemType() != other.getItemType()) return -1
        if (this.getInnerDataId() != other.getInnerDataId()) return -1
        return 0
    }
}