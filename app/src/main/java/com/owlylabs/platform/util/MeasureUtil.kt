package com.owlylabs.platform.util

import android.content.Context
import android.view.ViewGroup
import com.owlylabs.platform.R
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemAbstract
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemSingleNews
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemSingleVideo
import com.owlylabs.platform.model.data.tab_list_item_types.audio.TabListItemSingleAudio
import com.owlylabs.platform.model.data.tab_list_item_types.book.TabListItemSingleBook
import com.owlylabs.platform.constants.AppLogicConstants

class MeasureUtil {
    companion object {
        fun getWidthOfItem(
            context: Context,
            parent: ViewGroup,
            typeOfRecyclerViewList: TabListItemAbstract
        ): Int {
            return when (typeOfRecyclerViewList) {
                is TabListItemSingleVideo,
                is TabListItemSingleNews -> {
                    val numberOfPossibleElements =
                        FrameworkUtil.getNumberOfElements(context, R.integer.numberOfBigBanners)

                    var test = parent.measuredWidth
                    test = test - parent.paddingStart
                    test = test - parent.paddingEnd
                    test = test - context.resources.getDimensionPixelSize(R.dimen.tab_list_side_margin)*2
                    var sizeOFItemPadding =
                        context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin)
                    sizeOFItemPadding = sizeOFItemPadding * (numberOfPossibleElements - 1)
                    test = test - sizeOFItemPadding
                    test = test / numberOfPossibleElements

                    return test.toInt()

                }
                is TabListItemSingleBook,
                is TabListItemSingleAudio -> {
                    val numberOfPossibleElements =
                        FrameworkUtil.getNumberOfElements(context, R.integer.numberOfMiddleBanners)

                    var test = parent.measuredWidth
                    test = test - parent.paddingStart
                    test = test - parent.paddingEnd
                    var sizeOFItemPadding =
                        context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin)
                    sizeOFItemPadding = sizeOFItemPadding * (numberOfPossibleElements - 1)
                    test = test - sizeOFItemPadding
                    test = test - context.resources.getDimensionPixelSize(R.dimen.tab_list_side_margin)*2
                    test = test / numberOfPossibleElements

                    return test.toInt()

                }
                else -> 0
            }
        }

        fun getWidthOfItem(
            context: Context,
            parent: ViewGroup,
            typeOfRecyclerViewList: Int
        ): Int {
            return when (typeOfRecyclerViewList) {
                AppLogicConstants.BookCollectionViewHolderType.BOOK.ordinal,
                AppLogicConstants.TabViewHolderType.BOOK_HORIZONTAL_SECTION.ordinal,
                AppLogicConstants.TabViewHolderType.AUDIO_HORIZONTAL_SECTION.ordinal,
                AppLogicConstants.TabViewHolderType.VIDEO_HORIZONTAL_SECTION.ordinal -> {
                    val numberOfPossibleElements =
                        FrameworkUtil.getNumberOfElements(context, R.integer.numberOfMiddleBanners)

                    var test = parent.measuredWidth
                    test = test - context.resources.getDimensionPixelSize(R.dimen.tab_list_side_margin)*2
                    var sizeOFItemPadding =
                        context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin)
                    sizeOFItemPadding = sizeOFItemPadding * (numberOfPossibleElements - 1)
                    test = test - sizeOFItemPadding
                    test = test / numberOfPossibleElements

                    return test.toInt()
                }
                AppLogicConstants.TabViewHolderType.BANNER_HORIZONTAL_SECTION.ordinal -> {
                    val numberOfPossibleElements =
                        FrameworkUtil.getNumberOfElements(context, R.integer.numberOfBigBanners)

                    var test = parent.measuredWidth
                    test = test - parent.paddingStart
                    test = test - parent.paddingEnd
                    var sizeOFItemPadding =
                        context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin)
                    sizeOFItemPadding = sizeOFItemPadding * (numberOfPossibleElements - 1)
                    test = test - sizeOFItemPadding
                    test = test / numberOfPossibleElements
                    return test

                }
                else -> 0
            }
        }

        fun getWidthOfHorizontalBookItem(
            context: Context,
            parentWidth : Int
        ): Int {
            val numberOfPossibleElements = FrameworkUtil.getNumberOfElements(context, R.integer.numberOfMiddleBanners)

            var test = parentWidth
            test = test - (FrameworkUtil.convertDpToPixels(24f, context)*2).toInt()
            var sizeOFItemPadding = context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin)
            sizeOFItemPadding = sizeOFItemPadding * (numberOfPossibleElements - 1)
            test = test - sizeOFItemPadding
            test = test / numberOfPossibleElements

            return test.toInt()
        }

        fun getWidthOfHorizontalBannerItem(
            context: Context,
            parentWidth : Int
        ): Int {
            val numberOfPossibleElements = FrameworkUtil.getNumberOfElements(context, R.integer.numberOfBigBanners)

            var test = parentWidth
            test = test - (FrameworkUtil.convertDpToPixels(24f, context)*2).toInt()
            var sizeOFItemPadding = context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin)
            sizeOFItemPadding = sizeOFItemPadding * (numberOfPossibleElements - 1)
            test = test - sizeOFItemPadding
            test = test / numberOfPossibleElements

            return test.toInt()
        }
    }
}