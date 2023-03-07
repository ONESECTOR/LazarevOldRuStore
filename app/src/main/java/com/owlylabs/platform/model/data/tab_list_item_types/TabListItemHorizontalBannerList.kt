package com.owlylabs.platform.model.data.tab_list_item_types

import com.owlylabs.platform.model.data.BannerData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemHorizontalBannerList(sectionData: SectionData, data: List<BannerData>) : TabListItemAbstract(sectionData) {
    var data: List<BannerData>

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.BANNER_HORIZONTAL_SECTION.ordinal
    }

    override fun getInnerDataId(): Int {
        return sectionData.id_section
    }

    override fun getItemData(): Any {
        return this
    }


}