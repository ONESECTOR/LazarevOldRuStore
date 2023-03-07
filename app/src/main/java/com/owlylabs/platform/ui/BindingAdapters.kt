package com.owlylabs.platform.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.constants.AppLogicConstants



class BindingAdapters{
    companion object{
        @JvmStatic
        @BindingAdapter(AppLogicConstants.BINDING_ADAPTER_ENABLE_NAV_BAR_PADDING)
        fun setPadd(recyclerView: RecyclerView, isOn: String) {
            if (isOn.equals("yes") ){

                    val context = recyclerView.context
                    val resourceId: Int = context.resources.getIdentifier(
                        "design_bottom_navigation_height",
                        "dimen",
                        context.getPackageName()
                    )
                    var height = 0
                    if (resourceId > 0) {
                        height = context.resources.getDimensionPixelSize(resourceId)
                    }
                    recyclerView.setPadding(recyclerView.paddingStart,
                        recyclerView.paddingTop,
                        recyclerView.paddingEnd,
                        recyclerView.paddingBottom + height)
                }

            else {
                    val context = recyclerView.context
                    val resourceId: Int = context.resources.getIdentifier(
                        "design_bottom_navigation_height",
                        "dimen",
                        context.getPackageName()
                    )
                    var height = 0
                    if (resourceId > 0) {
                        height = context.resources.getDimensionPixelSize(resourceId)
                    }
                    recyclerView.setPadding(recyclerView.paddingStart,
                        recyclerView.paddingTop,
                        recyclerView.paddingEnd,
                        recyclerView.paddingBottom - height)

            }

        }

        @JvmStatic
        @BindingAdapter(AppLogicConstants.BINDING_ADAPTER_NEWS_IMG)
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                Glide.with(view.getContext())
                    .load(ApiConstants.API_BASE_URL.plus(imageUrl))
                    .centerCrop()
                    .into(view)
            }
        }

        @JvmStatic
        @BindingAdapter(AppLogicConstants.BINDING_ADAPTER_SELECTED_SOMU_SUB)
        fun checkIfUserSelectedSomeSubscription(view: MaterialButton, liveData: MutableLiveData<SkuDetails>?) {
            if (liveData != null){
                val liveDataValue = liveData.value
                liveDataValue?.let {
                    view.isEnabled = true
                } ?: kotlin.run {
                    view.isEnabled = false
                }
            }
        }
    }
}
