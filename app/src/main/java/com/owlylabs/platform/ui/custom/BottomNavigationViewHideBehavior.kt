package com.owlylabs.platform.ui.custom

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.Dimension
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationViewHideBehavior :
    CoordinatorLayout.Behavior<BottomNavigationView> {
    private var height = 0
    private var currentState =
        STATE_SCROLLED_UP
    private var additionalHiddenOffsetY = 0
    private var currentAnimator: ViewPropertyAnimator? = null
    private var onBottomNavigationViewStateChanged: IOnBottomNavigationViewStateChanged? = null

    constructor(listener: IOnBottomNavigationViewStateChanged) {
        this.onBottomNavigationViewStateChanged = listener
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
    }

    override fun onLayoutChild(
        parent: CoordinatorLayout, child: BottomNavigationView, layoutDirection: Int
    ): Boolean {
        val paramsCompat = child.layoutParams as MarginLayoutParams
        height = child.measuredHeight + paramsCompat.bottomMargin
        return super.onLayoutChild(parent, child, layoutDirection)
    }

    /**
     * Sets an additional offset for the y position used to hide the view.
     *
     * @param child the child view that is hidden by this behavior
     * @param offset the additional offset in pixels that should be added when the view slides away
     */
    fun setAdditionalHiddenOffsetY(child: BottomNavigationView, @Dimension offset: Int) {
        additionalHiddenOffsetY = offset
        if (currentState == STATE_SCROLLED_DOWN) {
            child.translationY = height + additionalHiddenOffsetY.toFloat()
        }
    }

    override fun onStartNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int
    ): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: BottomNavigationView,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int
    ) {
    }

    /**
     * Perform an animation that will slide the child from it's current position to be totally on the
     * screen.
     */
    fun slideUp(child: BottomNavigationView) {
        if (currentState == STATE_SCROLLED_UP) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        currentState =
            STATE_SCROLLED_UP
        animateChildTo(
            child,
            0,
            ENTER_ANIMATION_DURATION.toLong(),
            AccelerateDecelerateInterpolator(),
            true
        )
    }

    /**
     * Perform an animation that will slide the child from it's current position to be totally off the
     * screen.
     */
    fun slideDown(child: BottomNavigationView) {
        if (currentState == STATE_SCROLLED_DOWN) {
            return
        }
        if (currentAnimator != null) {
            currentAnimator!!.cancel()
            child.clearAnimation()
        }
        currentState =
            STATE_SCROLLED_DOWN
        animateChildTo(
            child,
            height + additionalHiddenOffsetY,
            EXIT_ANIMATION_DURATION.toLong(),
            DecelerateInterpolator(),
            false
        )
    }

    private fun animateChildTo(
        child: BottomNavigationView, targetY: Int, duration: Long, interpolator: TimeInterpolator, isExpanded: Boolean
    ) {

        currentAnimator = child
            .animate()
            .translationY(targetY.toFloat())
            .setInterpolator(interpolator)
            .setDuration(duration)
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        currentAnimator = null
                        onBottomNavigationViewStateChanged?.onBottomNavigationViewStateChanged(isExpanded)
                    }
                })

    }

    companion object {
        protected const val ENTER_ANIMATION_DURATION = 225
        protected const val EXIT_ANIMATION_DURATION = 175
        private const val STATE_SCROLLED_DOWN = 1
        private const val STATE_SCROLLED_UP = 2
    }

    interface IOnBottomNavigationViewStateChanged {
        fun onBottomNavigationViewStateChanged(isExpanded: Boolean)
    }
}