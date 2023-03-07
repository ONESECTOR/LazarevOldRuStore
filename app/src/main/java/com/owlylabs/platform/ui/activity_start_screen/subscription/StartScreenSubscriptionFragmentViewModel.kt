package com.owlylabs.platform.ui.activity_start_screen.subscription

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.billingclient.api.SkuDetails

class StartScreenSubscriptionFragmentViewModel : ViewModel() {
    val selectedSubscriptionLiveData = MutableLiveData<SkuDetails>()

}