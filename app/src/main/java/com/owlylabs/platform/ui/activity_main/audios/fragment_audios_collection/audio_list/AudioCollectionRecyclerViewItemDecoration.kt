package com.owlylabs.platform.ui.audios.fragment_audios_collection.audio_list

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.R


class AudioCollectionRecyclerViewItemDecoration(context: Context): RecyclerView.ItemDecoration() {

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
            is AudioCollectionRecyclerViewAdapter.AudioHolder -> {
                val adapterPos = parent.getChildAdapterPosition(view)

                val gridLayoutManager = parent.layoutManager as GridLayoutManager

                val lp = itemHolder.itemView.getLayoutParams() as GridLayoutManager.LayoutParams
                val itemColumnIndex = lp.spanIndex
                val itemColumnSize = lp.spanSize
                val columnsPerRow = gridLayoutManager.spanCount

                val rowIndex = gridLayoutManager.spanSizeLookup.getSpanGroupIndex(adapterPos, columnsPerRow)

                if (itemColumnIndex == 0){
                    outRect.left = itemSideSpace
                } else {
                    outRect.left = itemItemSpace
                }

                if (itemColumnIndex + itemColumnSize == columnsPerRow ){
                    outRect.right = itemSideSpace
                } else {
                    outRect.right = itemItemSpace
                }

                if (rowIndex == 0){
                    outRect.top = itemSideSpace
                } else {
                    outRect.top = itemItemSpace
                }

                if (rowIndex != gridLayoutManager.spanSizeLookup.getSpanGroupIndex(parent.adapter!!.itemCount-1,columnsPerRow )){
                    outRect.bottom = itemItemSpace
                } else{
                    outRect.bottom = itemSideSpace
                }
            }
            is AudioCollectionRecyclerViewAdapter.SubTitleHolder -> {
                outRect.top = 0
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
            }
        }

    }
}