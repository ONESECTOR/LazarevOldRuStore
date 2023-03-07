package com.owlylabs.platform.util

import android.animation.ArgbEvaluator
import android.animation.StateListAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import com.owlylabs.platform.R
import kotlin.properties.Delegates


class ViewUtil {
    companion object {
        fun getAppBarLayoutStateListAnimatorForDetailFragments(context: Context, view: View):
                StateListAnimator {

            val stateScrolledValueAnimator = ValueAnimator.ofFloat(0f, 1f)
            stateScrolledValueAnimator.duration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            stateScrolledValueAnimator.addUpdateListener(object: ValueAnimator.AnimatorUpdateListener{

                var animationJustStarted = false
                var animateFromColor: Int by Delegates.notNull()

                override fun onAnimationUpdate(animation: ValueAnimator) {

                    animationJustStarted = animation.animatedValue == 0F
                    if (animationJustStarted){
                        if (view.background is ColorDrawable){
                            animateFromColor =  (view.background as ColorDrawable).color
                        } else {
                            animateFromColor = ContextCompat.getColor(context, R.color.transparent_but_white)
                        }
                    }
                    val color =  ArgbEvaluator().evaluate(animation.animatedFraction, animateFromColor, ContextCompat.getColor(context, R.color.action_bar_background)) as Int
                    view.background = ColorDrawable(color)

                    view.elevation = context.resources.getDimension(R.dimen.design_appbar_elevation) * animation.animatedFraction
                }
            })

            val stateIdleValueAnimator = ValueAnimator.ofFloat(0f, 1f)
            stateIdleValueAnimator.duration = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()
            stateIdleValueAnimator.addUpdateListener(object: ValueAnimator.AnimatorUpdateListener{

                var animationJustStarted = false
                var animateFromColor: Int by Delegates.notNull()

                override fun onAnimationUpdate(animation: ValueAnimator) {

                    animationJustStarted = animation.animatedValue == 0F
                    if (animationJustStarted){
                        if (view.background is ColorDrawable){
                            animateFromColor =  (view.background as ColorDrawable).color
                        } else {
                            animateFromColor = ContextCompat.getColor(context, R.color.transparent_but_white)
                        }
                    }
                    if(animateFromColor != ContextCompat.getColor(context, R.color.transparent_but_white)){
                        val color = ArgbEvaluator().evaluate(animation.animatedFraction, animateFromColor, ContextCompat.getColor(context, R.color.transparent_but_white)) as Int
                        view.background = ColorDrawable(color)
                    }
                    if (view.elevation != 0F){
                        view.elevation = context.resources.getDimension(R.dimen.design_appbar_elevation) * (1f - animation.animatedFraction)
                    }
                }
            })

            val stateListAnimator = StateListAnimator()
            stateListAnimator.addState(
                intArrayOf(
                    android.R.attr.state_selected
                ), stateIdleValueAnimator
            )
            stateListAnimator.addState(
                IntArray(0), stateScrolledValueAnimator
            )
            return stateListAnimator
        }

        fun setLightStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var flags =
                    activity.window.decorView.systemUiVisibility // get current flag
                flags =
                    flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // add LIGHT_STATUS_BAR to flag
                activity.window.decorView.systemUiVisibility = flags
            }
        }

        fun clearLightStatusBar(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var flags =
                    activity.window.decorView.systemUiVisibility // get current flag
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                activity.window.decorView.systemUiVisibility = flags
            }
        }
    }
}