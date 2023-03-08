package com.owlylabs.platform.ui.activity_main.subscription.model

sealed class BillingEvent {
    data class ShowDialog(val dialogInfo: InfoDialogState): BillingEvent()
    data class ShowError(val error: Throwable): BillingEvent()
}