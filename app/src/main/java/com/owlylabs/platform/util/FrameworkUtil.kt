package com.owlylabs.platform.util

import android.R
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue


class FrameworkUtil {
    companion object{
        fun getToolbarHeight(context: Context): Int {
            var height = 0
            val tv = TypedValue()
            if (context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(
                    tv.data,
                    context.resources.displayMetrics
                )
            }
            return height
        }

        fun getBottomNavigationViewHeight(context: Context) : Int{
            val resourceId: Int = context.resources.getIdentifier(
                "design_bottom_navigation_height",
                "dimen",
                context.getPackageName()
            )
            var height = 0
            if (resourceId > 0) {
                height = context.resources.getDimensionPixelSize(resourceId)
            }
            return height
        }

        fun getStatusBarHeight(context: Context): Int {
            var result = 0
            val resourceId: Int =
                context.getResources().getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId)
            }
            return result
        }

        fun getNumberOfElements(context: Context, intId: Int): Int {
            return context.resources.getInteger(intId)
        }

        fun getDimenstionInPixels(context: Context, dimenId: Int): Int {
            return context.resources.getDimension(dimenId).toInt()
        }

        fun convertDpToPixels(
            dp: Float, context: Context
        ): Float {
            return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
        }
    }
}