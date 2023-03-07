package com.owlylabs.platform.ui.custom.tab

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.constants.AppLogicConstants

open class VerticalRecyclerViewThatUsedInTabs : RecyclerView {

    private var scrollPointerId = -1
    private var pointTouchX = 0
    private var pointTouchY = 0
    private var touchSlopType = 0
    private var velocityX2 : Int = 0;

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val vc = ViewConfiguration.get(context)
        touchSlopType = vc.scaledTouchSlop
    }

    override fun setScrollingTouchSlop(slopConstant: Int) {
        super.setScrollingTouchSlop(slopConstant)

        val vc = ViewConfiguration.get(context)
        when (slopConstant) {
            TOUCH_SLOP_DEFAULT -> touchSlopType = vc.scaledTouchSlop
            TOUCH_SLOP_PAGING -> touchSlopType = vc.scaledPagingTouchSlop
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        if (e == null) {
            return false
        }

        val action = e.actionMasked
        val actionIndex = e.actionIndex

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                scrollPointerId = e.getPointerId(0)
                pointTouchX = Math.round(e.x + 0.5f)
                pointTouchY = Math.round(e.y + 0.5f)
                return super.onInterceptTouchEvent(e)
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                scrollPointerId = e.getPointerId(actionIndex)
                pointTouchX = Math.round(e.getX(actionIndex) + 0.5f)
                pointTouchY = Math.round(e.getY(actionIndex) + 0.5f)
                return super.onInterceptTouchEvent(e)
            }

            MotionEvent.ACTION_MOVE -> {
                val index = e.findPointerIndex(scrollPointerId)
                if (index < 0) {
                    return false
                }

                val x = Math.round(e.getX(index) + 0.5f)
                val y = Math.round(e.getY(index) + 0.5f)
                if (scrollState != SCROLL_STATE_DRAGGING) {
                    val dx = x - pointTouchX
                    val dy = y - pointTouchY
                    var startScroll = false
                    if (layoutManager?.canScrollHorizontally() == true && Math.abs(dx) > touchSlopType && (layoutManager?.canScrollVertically() == true || Math.abs(dx) > Math.abs(dy))) {
                        startScroll = true
                    }
                    if (layoutManager?.canScrollVertically() == true && Math.abs(dy) > touchSlopType && (layoutManager?.canScrollHorizontally() == true || Math.abs(dy) > Math.abs(dx))) {
                        startScroll = true
                    }
                    return startScroll && super.onInterceptTouchEvent(e)
                }
                return super.onInterceptTouchEvent(e)
            }
            else -> {
                return super.onInterceptTouchEvent(e)
            }
        }
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        velocityX2 = (velocityX * AppLogicConstants.RECYCLERVIEW_VELOCITY).toInt()
        return super.fling(velocityX2, velocityY)
    }
}