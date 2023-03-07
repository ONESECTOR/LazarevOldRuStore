package com.owlylabs.platform.ui.tab_with_sections

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.R


class TabThatRepresentsSectionsRecyclerViewItemDecorations(context: Context): RecyclerView.ItemDecoration() {

    val spanCount: Int
    val itemSideSpace: Int
    val itemItemSpace: Int
    val newsSeparatorLineWidth: Int
    val dividerPaint: Paint

    init {
        spanCount = context.resources.getInteger(R.integer.numberOfMaxPossibleElements)
        itemSideSpace = context.resources.getDimensionPixelSize(R.dimen.tab_list_side_margin)
        itemItemSpace = context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin_half)
        newsSeparatorLineWidth = context.resources.getDimensionPixelSize(R.dimen.common_list_divider_height)
        dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dividerPaint.color = ContextCompat.getColor(context, R.color.Divider)
        dividerPaint.strokeWidth = newsSeparatorLineWidth.toFloat()
        dividerPaint.style = Paint.Style.FILL
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        adapter?.let { recyclerViewAdapter ->
            for (i in 0 until parent.childCount) {
                val itemView = parent.getChildAt(i) as ViewGroup
                val itemHolder = parent.getChildViewHolder(itemView)
                when (itemHolder) {
                    is TabThatRepresentsSectionsRecyclerViewAdapter.NewsHolder -> {
                        val adapterPos = parent.getChildAdapterPosition(itemView)

                        val gridLayoutManager = parent.layoutManager as GridLayoutManager
                        val columnsPerRow = gridLayoutManager.spanCount

                        val lp = itemHolder.itemView.getLayoutParams() as GridLayoutManager.LayoutParams
                        val itemColumnIndex = lp.spanIndex
                        val itemColumnSize = lp.spanSize

                        val rowIndex = gridLayoutManager.spanSizeLookup.getSpanGroupIndex(adapterPos, columnsPerRow)

                        if (columnsPerRow == itemColumnSize){
                            if (rowIndex != gridLayoutManager.spanSizeLookup.getSpanGroupIndex(parent.adapter!!.itemCount-1,columnsPerRow )){
                                val dividerPositionXStart = itemView.left.toFloat()
                                val dividerPositionXEnd = itemView.right.toFloat()
                                val dividerPositionYStart = itemView.bottom.toFloat() + itemSideSpace
                                val dividerPositionYEnd = itemView.bottom.toFloat() + itemSideSpace +  newsSeparatorLineWidth
                                c.drawLine(
                                    dividerPositionXStart,
                                    dividerPositionYStart,
                                    dividerPositionXEnd,
                                    dividerPositionYEnd,
                                    dividerPaint
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemHolder = parent.getChildViewHolder(view)
        when (itemHolder){
            is TabThatRepresentsSectionsRecyclerViewAdapter.NewsHolder,
            is TabThatRepresentsSectionsRecyclerViewAdapter.AudioHolder,
            is TabThatRepresentsSectionsRecyclerViewAdapter.BookHolder,
            is TabThatRepresentsSectionsRecyclerViewAdapter.VideoHolder -> {
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
                    outRect.top = 0
                } else {
                    outRect.top = itemItemSpace
                }

                if (itemHolder is TabThatRepresentsSectionsRecyclerViewAdapter.NewsHolder){
                    if (columnsPerRow == itemColumnSize){
                        if (rowIndex != gridLayoutManager.spanSizeLookup.getSpanGroupIndex(parent.adapter!!.itemCount-1,columnsPerRow )){
                            outRect.bottom = itemSideSpace + newsSeparatorLineWidth + itemSideSpace
                        } else{
                            outRect.bottom = 0
                        }
                    } else {
                        if (rowIndex != gridLayoutManager.spanSizeLookup.getSpanGroupIndex(parent.adapter!!.itemCount-1,columnsPerRow )){
                            outRect.bottom = itemItemSpace
                        } else{
                            outRect.bottom = 0
                        }
                    }
                } else {
                    if (rowIndex != gridLayoutManager.spanSizeLookup.getSpanGroupIndex(parent.adapter!!.itemCount-1,columnsPerRow )){
                        outRect.bottom = itemItemSpace
                    } else{
                        outRect.bottom = 0
                    }
                }


            }
            is TabThatRepresentsSectionsRecyclerViewAdapter.TitleHolder -> {
                val adapterPos = parent.getChildAdapterPosition(view)
                if (adapterPos == 0) {
                    outRect.top = 0
                } else {
                    outRect.top = itemItemSpace
                }
                outRect.bottom = 0
                outRect.right = 0
                outRect.left = 0
            }
            is TabThatRepresentsSectionsRecyclerViewAdapter.BannerHorizontalListHolder,
            is TabThatRepresentsSectionsRecyclerViewAdapter.AudioHorizontalListHolder,
            is TabThatRepresentsSectionsRecyclerViewAdapter.BookHorizontalListHolder,
            is TabThatRepresentsSectionsRecyclerViewAdapter.SubTitleHolder -> {
                val adapterPos = parent.getChildAdapterPosition(view)
                if (adapterPos == 0) {
                    outRect.top = 0
                } else {
                    outRect.top = itemItemSpace
                }
                outRect.bottom = itemItemSpace
                outRect.right = 0
                outRect.left = 0
            }
            is TabThatRepresentsSectionsRecyclerViewAdapter.SeparatorHolder -> {
                outRect.top = itemItemSpace
                outRect.bottom = itemItemSpace
                outRect.right = 0
                outRect.left = 0
            }
        }

    }
}