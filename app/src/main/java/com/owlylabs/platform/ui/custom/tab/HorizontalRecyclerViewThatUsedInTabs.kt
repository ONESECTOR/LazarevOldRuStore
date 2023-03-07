package com.owlylabs.platform.ui.custom.tab

import android.content.Context
import android.util.AttributeSet

class HorizontalRecyclerViewThatUsedInTabs :
    VerticalRecyclerViewThatUsedInTabs {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
}