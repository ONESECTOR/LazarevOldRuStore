package com.owlylabs.platform.ui.activity_main.subscription.model

import androidx.annotation.StringRes

data class InfoDialogState(
    @StringRes val titleRes: Int,
    val message: String
)