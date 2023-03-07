package com.owlylabs.platform.ui.tab_with_sections

import android.net.Uri

interface INavToFragmentByDeepLink {
    fun navigateToFragmentByDeepLink(uri: Uri)
}