package com.owlylabs.platform.ui.activity_main.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owlylabs.platform.ui.activity_main.subscription.model.BillingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SubscriptionViewModel: ViewModel() {

    private val _state = MutableStateFlow(BillingState())
    val state = _state.asStateFlow()

    init {

    }

    class Factory : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SubscriptionViewModel() as T
        }
    }
}