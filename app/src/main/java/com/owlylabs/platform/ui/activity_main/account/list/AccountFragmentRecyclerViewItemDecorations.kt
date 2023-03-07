package com.owlylabs.platform.ui.account.list

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
import com.owlylabs.platform.constants.AppLogicConstants


class AccountFragmentRecyclerViewItemDecorations(context: Context) : RecyclerView.ItemDecoration() {

    val spanCount: Int
    val itemSideSpace: Int
    val itemItemSpace: Int
    val actionSpace: Int
    val dividerPaint: Paint

    init {
        spanCount = context.resources.getInteger(R.integer.numberOfMaxPossibleElements)
        itemSideSpace = context.resources.getDimensionPixelSize(R.dimen.tab_list_side_margin)
        itemItemSpace = context.resources.getDimensionPixelSize(R.dimen.tab_list_item_margin_half)
        actionSpace = context.resources.getDimensionPixelSize(R.dimen.fragment_account_holder_action_separator_height)
        dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dividerPaint.color = ContextCompat.getColor(context, R.color.Divider)
        dividerPaint.strokeWidth = actionSpace.toFloat()
        dividerPaint.style = Paint.Style.FILL
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemHolder = parent.getChildViewHolder(view)
        when (itemHolder) {
            is AccountFragmentRecyclerViewAdapter.NameHolder -> {
                outRect.top = 0
                outRect.bottom = itemItemSpace
                outRect.left = 0
                outRect.right = 0
            }
            is AccountFragmentRecyclerViewAdapter.ActionHolder -> {

                val adapterPos = parent.getChildAdapterPosition(view)

                if (adapterPos != 0) {
                    parent.adapter?.let { adapter ->
                        if (adapter.getItemViewType(adapterPos - 1) != AppLogicConstants.AccountListHolderType.ACTION_HOLDER.ordinal) {
                            outRect.top = actionSpace
                        } else {
                            outRect.top = 0
                        }
                    } ?: run {
                        outRect.top = 0
                    }
                    outRect.bottom = 0
                    outRect.left = 0
                    outRect.right = 0
                }
            }
            is AccountFragmentRecyclerViewAdapter.BannerHolder -> {

                val adapterPos = parent.getChildAdapterPosition(view)

                val gridLayoutManager = parent.layoutManager as GridLayoutManager

                val lp = itemHolder.itemView.getLayoutParams() as GridLayoutManager.LayoutParams
                val itemColumnIndex = lp.spanIndex
                val itemColumnSize = lp.spanSize
                val columnsPerRow = gridLayoutManager.spanCount

                val rowIndex =
                    gridLayoutManager.spanSizeLookup.getSpanGroupIndex(adapterPos, columnsPerRow)

                if (itemColumnIndex == 0) {
                    outRect.left = itemSideSpace
                } else {
                    outRect.left = itemItemSpace
                }

                if (itemColumnIndex + itemColumnSize == columnsPerRow) {
                    outRect.right = itemSideSpace
                } else {
                    outRect.right = itemItemSpace
                }

                outRect.top = itemItemSpace * 2
                outRect.bottom = 0
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        adapter?.let { recyclerViewAdapter ->
            for (i in 0 until parent.childCount) {
                val itemView = parent.getChildAt(i) as ViewGroup
                val itemHolder = parent.getChildViewHolder(itemView)
                when (itemHolder){
                    is AccountFragmentRecyclerViewAdapter.ActionHolder -> {
                        val adapterPos = parent.getChildAdapterPosition(itemView)
                        var dividerPositionXStart = 0F
                        var dividerPositionXEnd = parent.width.toFloat()
                        var dividerPositionYStart = itemView.top.toFloat() - actionSpace
                        var dividerPositionYEnd = itemView.top.toFloat()
                        if (adapter.getItemViewType(adapterPos - 1) != AppLogicConstants.AccountListHolderType.ACTION_HOLDER.ordinal) {
                            c.drawLine(
                                dividerPositionXStart,
                                dividerPositionYStart,
                                dividerPositionXEnd,
                                dividerPositionYEnd,
                                dividerPaint
                            )
                        }
                        dividerPositionXStart = 0F
                        dividerPositionXEnd = parent.width.toFloat()
                        dividerPositionYStart = itemView.bottom.toFloat()
                        dividerPositionYEnd = itemView.bottom.toFloat() + actionSpace
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