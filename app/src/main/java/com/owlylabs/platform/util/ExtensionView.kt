package com.owlylabs.platform.util

import android.os.Build
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.owlylabs.platform.R
import com.owlylabs.platform.helpers.SafeClickListener

fun View.insetPadding() {
    var topPadding = FrameworkUtil.getToolbarHeight(context)
    val bottomPadding = resources.getDimensionPixelSize(R.dimen.spacing_24) +
            FrameworkUtil.getBottomNavigationViewHeight(context)

    topPadding += if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        FrameworkUtil.getStatusBarHeight(context)
    } else {
        resources.getDimensionPixelSize(R.dimen.spacing_24)
    }

    this.updatePadding(
        top = topPadding,
        bottom = bottomPadding
    )
}

fun View.setSafeOnClickListener(intervalTime: Int = 3000, onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener(intervalTime) {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

fun AppCompatActivity.setupBottomNavigationView() {
    val navController = this.findNavController(R.id.nav_host_fragment)
    val navView: BottomNavigationView = findViewById(R.id.nav_view)
    navView.setupWithNavController(navController)
}