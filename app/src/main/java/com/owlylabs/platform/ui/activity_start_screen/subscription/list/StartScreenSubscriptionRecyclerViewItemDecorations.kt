package com.owlylabs.platform.ui.tab_with_sections

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.R
import com.owlylabs.platform.ui.activity_start_screen.subscription.list.StartScreenSubscriptionRecyclerViewAdapter
import com.owlylabs.platform.constants.AppLogicConstants


class StartScreenSubscriptionRecyclerViewItemDecorations(context: Context) :
    RecyclerView.ItemDecoration() {

    val space_between_header_and_action_list: Int
    val space_between_actions: Int
    val space_between_action_list_and_subscription_list: Int
    val space_between_subscriptions: Int
    val space_between_subscription_list_and_footer: Int
    val dividerPaint: Paint

    init {
        space_between_header_and_action_list =
            context.resources.getDimensionPixelSize(R.dimen.fragment_start_screen_subscription_space_between_header_and_action_list)
        space_between_actions =
            context.resources.getDimensionPixelSize(R.dimen.fragment_start_screen_subscription_space_between_actions)
        space_between_action_list_and_subscription_list =
            context.resources.getDimensionPixelSize(R.dimen.fragment_start_screen_subscription_space_between_action_list_and_subscription_list)
        space_between_subscriptions =
            context.resources.getDimensionPixelSize(R.dimen.fragment_start_screen_subscription_space_between_subscriptions)
        space_between_subscription_list_and_footer =
            context.resources.getDimensionPixelSize(R.dimen.fragment_start_screen_subscription_space_between_subscription_list_and_footer)
        dividerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        dividerPaint.color = ContextCompat.getColor(context, R.color.fragment_subscriptions_footer)
        dividerPaint.style = Paint.Style.FILL
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter
        adapter?.let { recyclerViewAdapter ->
            for (i in 0 until parent.childCount) {
                val itemView = parent.getChildAt(i) as View
                val itemHolder = parent.getChildViewHolder(itemView)
                when (itemHolder) {
                    is StartScreenSubscriptionRecyclerViewAdapter.FooterHolder -> {

                        val dividerPositionXStart = itemView.left.toFloat()
                        val dividerPositionXEnd = itemView.right.toFloat()
                        val dividerPositionYStart = itemView.bottom.toFloat()
                        val dividerPositionYEnd = parent.bottom.toFloat()
                        c.drawRect(
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

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val adapter = parent.adapter
        val adapterPos = parent.getChildAdapterPosition(view)
        if (adapter is StartScreenSubscriptionRecyclerViewAdapter) {
            val itemHolder = parent.getChildViewHolder(view)
            when (itemHolder) {
                is StartScreenSubscriptionRecyclerViewAdapter.HeaderHolder -> {
                    outRect.top = 0
                }
                is StartScreenSubscriptionRecyclerViewAdapter.ActionHolder -> {
                    if (adapterPos > 0
                        && adapter.getItemViewType(adapterPos - 1) != AppLogicConstants.StartScreenSubscriptionHolderType.ACTION_HOLDER.ordinal
                    ) {
                        outRect.top = space_between_header_and_action_list
                    } else {
                        outRect.top = space_between_actions
                    }
                }
                is StartScreenSubscriptionRecyclerViewAdapter.SubscriptionHolder -> {
                    if (adapterPos > 0
                        && adapter.getItemViewType(adapterPos - 1) != AppLogicConstants.StartScreenSubscriptionHolderType.SUBSCRIPTION_HOLDER.ordinal
                    ) {
                        outRect.top = space_between_action_list_and_subscription_list
                    } else {
                        outRect.top = space_between_subscriptions
                    }
                }
                is StartScreenSubscriptionRecyclerViewAdapter.FooterHolder -> {
                    outRect.top = space_between_subscription_list_and_footer
                }
            }
        }
    }
}