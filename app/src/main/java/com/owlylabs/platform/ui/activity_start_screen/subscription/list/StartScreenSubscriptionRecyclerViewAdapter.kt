package com.owlylabs.platform.ui.activity_start_screen.subscription.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails
import com.owlylabs.platform.BR
import com.owlylabs.platform.constants.BillingConstants
import com.owlylabs.platform.databinding.*
import com.owlylabs.platform.ui.activity_start_screen.subscription.StartScreenSubscriptionFragmentViewModel
import com.owlylabs.platform.ui.activity_start_screen.subscription.list.data.*
import com.owlylabs.platform.util.TextUtil
import com.owlylabs.platform.util.setSafeOnClickListener
import com.owlylabs.platform.constants.AppLogicConstants

class StartScreenSubscriptionRecyclerViewAdapter(
    val listener: StartScreenSubscriptionRecyclerViewListener,
    val startScreenSubscriptionFragmentViewModel: StartScreenSubscriptionFragmentViewModel) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<StartScreenSubscriptionRecyclerViewItemAbstract>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            AppLogicConstants.StartScreenSubscriptionHolderType.HEADER_HOLDER.ordinal ->
                return HeaderHolder(
                    FragmentStartScreenSubscriptionHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            AppLogicConstants.StartScreenSubscriptionHolderType.ACTION_HOLDER.ordinal ->
                return ActionHolder(
                    FragmentStartScreenSubscriptionActionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            AppLogicConstants.StartScreenSubscriptionHolderType.SUBSCRIPTION_HOLDER.ordinal ->
                return SubscriptionHolder(
                    FragmentStartScreenSubscriptionSubscriptionItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            AppLogicConstants.StartScreenSubscriptionHolderType.FOOTER_HOLDER.ordinal -> {
                val binding = FragmentStartScreenSubscriptionFooterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                binding.setVariable(BR.viewModel, startScreenSubscriptionFragmentViewModel)
                //binding.viewModel = startScreenSubscriptionFragmentViewModel
                return FooterHolder(binding)
            }
            else -> throw RuntimeException("There is no viewHolder for this item")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data[position].getItemType()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AbstractBindableHolder) {
            holder.bind(data[position])
        }
    }

    inner class HeaderHolder(
        val holderViewBinding: FragmentStartScreenSubscriptionHeaderBinding
    ) : RecyclerView.ViewHolder(holderViewBinding.root) {
        init {
            holderViewBinding.restore.setOnClickListener {
                listener.onClickRestartBilling()
            }
            holderViewBinding.btnBack.setOnClickListener {
                listener.onClickBack()
            }
        }
    }

    inner class ActionHolder(
        val holderViewBinding: FragmentStartScreenSubscriptionActionBinding
    ) : AbstractBindableHolder(holderViewBinding.root) {
        override fun bind(data: StartScreenSubscriptionRecyclerViewItemAbstract) {
            if (data is StartScreenSubscriptionRecyclerViewItemAction) {
                holderViewBinding.textViewAction.text = data.text
            }
        }
    }

    inner class SubscriptionHolder(
        val holderViewBinding: FragmentStartScreenSubscriptionSubscriptionItemBinding
    ) : AbstractBindableHolder(holderViewBinding.root) {

        init {
            holderViewBinding.parent.setOnClickListener {
                val itemData = data[adapterPosition]
                if (itemData is StartScreenSubscriptionRecyclerViewItemSubscription) {
                    listener.onClickSelectSubscription(itemData.skuDetail)
                }
            }
        }

        override fun bind(data: StartScreenSubscriptionRecyclerViewItemAbstract) {
            if (data is StartScreenSubscriptionRecyclerViewItemSubscription) {
                holderViewBinding.parent.isSelected = data.isSelected
                holderViewBinding.textViewTitle.text = TextUtil.getTitleForSubscriptionItemHolder(
                    holderViewBinding.root.context,
                    data.skuDetail
                )
                holderViewBinding.textViewPrice.text = TextUtil.getPriceForSubscriptionItemHolder(
                    holderViewBinding.root.context,
                    data.skuDetail
                )
            }
        }
    }

    inner class FooterHolder(
         val holderViewBinding: FragmentStartScreenSubscriptionFooterBinding
    ) : AbstractBindableHolder(holderViewBinding.root) {
        init {
            holderViewBinding.buttonSubscribe.setOnClickListener {
                data.forEach {
                    if (it is StartScreenSubscriptionRecyclerViewItemSubscription) {
                        if (it.isSelected) {
                            listener.onClickSubscribe(it.skuDetail)
                        }
                    }
                }
            }
            holderViewBinding.buttonSupport.setOnClickListener {
                listener.onClickSupport()
            }
            holderViewBinding.buttonTerms.setSafeOnClickListener {
                listener.onClickTermsOfUsage(BillingConstants.TERMS_OF_USE_URL)
            }
        }

        override fun bind(data: StartScreenSubscriptionRecyclerViewItemAbstract) {
            holderViewBinding.executePendingBindings()
        }
    }

    inner abstract class AbstractBindableHolder(holderView: View) :
        RecyclerView.ViewHolder(holderView) {
        open fun bind(data: StartScreenSubscriptionRecyclerViewItemAbstract) {}
    }

    fun updateData(newData: List<StartScreenSubscriptionRecyclerViewItemAbstract>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    fun updateSelectedState(skuDetails: SkuDetails) {
        data.forEach {
            if (it is StartScreenSubscriptionRecyclerViewItemSubscription) {
                if (!it.isSelected) {
                    if (it.skuDetail.sku == skuDetails.sku) {
                        it.isSelected = true
                        notifyItemChanged(data.indexOf(it))
                    }
                } else {
                    if (it.skuDetail.sku != skuDetails.sku){
                        it.isSelected = false
                        notifyItemChanged(data.indexOf(it))
                    }
                }
            }
            if (it is StartScreenSubscriptionRecyclerViewItemFooter){
                notifyItemChanged(data.indexOf(it))
            }
        }
    }
}