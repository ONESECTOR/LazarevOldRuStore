package com.owlylabs.platform.ui.news.fragment_news

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.R

class NewsRecyclerViewitemDecoration(context : Context) : RecyclerView.ItemDecoration() {

        private val dividerPaint : Paint
        private val dividerMargin = context.resources.getDimensionPixelSize(R.dimen.common_list_divider_margin)
        private val dividerHeight = context.resources.getDimensionPixelSize(R.dimen.common_list_divider_height)

        init {
                dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
                dividerPaint.color = ContextCompat.getColor(context, R.color.Divider)
                dividerPaint.strokeWidth = dividerHeight.toFloat()
                dividerPaint.style = Paint.Style.FILL
            }

        override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val adapter = parent.adapter
                adapter?.let { recyclerViewAdapter ->
                        for (i in 0 until parent.childCount) {
                                if (i + 1 != parent.childCount){
                                        val itemView = parent.getChildAt(i) as ViewGroup

                                        val dividerPositionXStart = (parent.paddingLeft + itemView.paddingLeft).toFloat()
                                       val dividerPositionXEnd = (parent.width - parent.paddingEnd - itemView.paddingEnd).toFloat()
                                        val dividerPositionYStart= itemView.bottom.toFloat() - dividerHeight
                                       val dividerPositionYEnd= itemView.bottom.toFloat()
                                        c.drawLine(dividerPositionXStart, dividerPositionYStart, dividerPositionXEnd, dividerPositionYEnd, dividerPaint)
                                    }
                            }
                    }
            }
    }