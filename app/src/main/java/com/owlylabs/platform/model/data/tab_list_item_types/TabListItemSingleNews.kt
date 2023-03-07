package com.owlylabs.platform.model.data.tab_list_item_types

import com.owlylabs.platform.model.data.NewsData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.constants.AppLogicConstants

class TabListItemSingleNews(sectionData: SectionData, data: NewsData) : TabListItemAbstract(sectionData) {
    var data: NewsData

    init {
        this.data = data
    }

    override fun getItemType(): Int {
        return AppLogicConstants.TabViewHolderType.NEWS_ITEM.ordinal
    }

    override fun getInnerDataId(): Int {
        return data.id.toInt()
    }

    override fun getItemData(): Any {
        return this
    }
}