package com.owlylabs.platform.ui.tab_with_sections.childrens

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.R


class HorizontalListRecyclerViewItemDecorations(context: Context): RecyclerView.ItemDecoration() {

    val spanCount: Int
    val itemSideSpace: Int
    val itemItemSpace: Int

    init {
        spanCount = context.resources.getInteger(R.integer.numberOfMaxPossibleElements)
        itemSideSpace = context.resources.getDimensionPixelSize(R.dimen.tab_list_side_margin)
        itemItemSpace = context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin_half)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemHolder = parent.getChildViewHolder(view)
        when (itemHolder){
            is HorizontalListAudioRecyclerViewAdapter.HorizontalListItem,
            is HorizontalListBannerRecyclerViewAdapter.HorizontalListItem,
            is HorizontalListBookRecyclerViewAdapter.HorizontalListItem -> {
                val adapterPos = parent.getChildAdapterPosition(view)

                if (adapterPos == 0){
                    outRect.left = itemSideSpace
                } else {
                    outRect.left = itemItemSpace
                }

                if (adapterPos + 1 == parent.adapter!!.itemCount ){
                    outRect.right = itemSideSpace
                } else {
                    outRect.right = itemItemSpace
                }
            }
        }

    }
}