package com.owlylabs.platform.ui.account.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.data.RecommendedData
import com.owlylabs.platform.ui.account.list.data.AccountFragmentListItemAbstract
import com.owlylabs.platform.ui.account.list.data.AccountFragmentListItemAction
import com.owlylabs.platform.ui.account.list.data.AccountFragmentListItemBanner
import com.owlylabs.platform.ui.account.list.data.AccountFragmentListItemName
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.constants.AppLogicConstants
import java.lang.RuntimeException

class AccountFragmentRecyclerViewAdapter(
    val listener: ItemListClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<AccountFragmentListItemAbstract>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            AppLogicConstants.AccountListHolderType.NAME_HOLDER.ordinal -> {
                return NameHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.fragment_account_list_item_holder_name,
                        parent,
                        false
                    )
                )
            }
            AppLogicConstants.AccountListHolderType.ACTION_HOLDER.ordinal -> {
                return ActionHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.fragment_account_list_item_holder_action,
                        parent,
                        false
                    )
                )
            }
            AppLogicConstants.AccountListHolderType.BANNER_HOLDER.ordinal -> {
                return BannerHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.fragment_account_list_item_holder_banner,
                        parent,
                        false
                    )
                )
            }
            AppLogicConstants.AccountListHolderType.FOOTER_HOLDER.ordinal -> {
                return FooterHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.fragment_account_list_item_holder_footer,
                        parent,
                        false
                    )
                )
            }
            else -> throw RuntimeException("unknown holder")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = data.get(position)
        when (holder) {
            is ActionHolder -> {
                if (itemData is AccountFragmentListItemAction) {
                    holder.textViewAction.text = itemData.actionName
                }
            }
            is NameHolder -> {
                if (itemData is AccountFragmentListItemName) {
                    if (itemData.name.isNotEmpty()){
                        holder.textViewCurrentName.text = itemData.name
                        holder.textViewCurrentName.visibility = View.VISIBLE
                    } else {
                        holder.textViewCurrentName.visibility = View.GONE
                    }
                }
            }
            is BannerHolder -> {
                if (itemData is AccountFragmentListItemBanner) {
                    Glide
                        .with(holder.banner_img)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.recommendedData.url_large_img))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .transform(CenterCrop(), RoundedCorners(FrameworkUtil.getDimenstionInPixels(holder.itemView.context, R.dimen.banner_list_item_horizontal_item_radius )))
                        .into(holder.banner_img)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return data.get(position).getItemType()
    }

    inner class NameHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val textViewCurrentName: TextView = itemView.findViewById(R.id.txt_current_name)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onChangeName()
        }
    }

    inner class ActionHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val textViewAction: TextView = itemView.findViewById(R.id.textView_action)

        init {
            textViewAction.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v is TextView) {
                when (v.text) {
                    v.context.resources.getString(R.string.manage_subscription) -> {
                        listener.onClickManageSubscription()
                    }
                    v.context.resources.getString(R.string.write_to_support) -> {
                        listener.onContactSupport()
                    }
                    v.context.resources.getString(R.string.rate_the_app) -> {
                        listener.onRateThisApp()
                    }
                    v.context.resources.getString(R.string.share_with_app) -> {
                        listener.onRecommendThisApp()
                    }
                }
            }
        }
    }

    inner class BannerHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        val banner_img: ImageView = itemView.findViewById(R.id.banner_img)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val recommendedData = (data.get(adapterPosition) as AccountFragmentListItemBanner).recommendedData
            listener.onBannerClick(recommendedData)
        }
    }

    inner class FooterHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            listener.onDeveloperLogoClick()
        }
    }

    fun updateData(newData: ArrayList<AccountFragmentListItemAbstract>) {
        this.data.clear()
        this.data.addAll(newData)
        notifyDataSetChanged()
    }

    interface ItemListClickListener {
        fun onChangeName()
        fun onClickManageSubscription()
        fun onContactSupport()
        fun onRateThisApp()
        fun onRecommendThisApp()
        fun onBannerClick(banner: RecommendedData)
        fun onDeveloperLogoClick()
    }
}