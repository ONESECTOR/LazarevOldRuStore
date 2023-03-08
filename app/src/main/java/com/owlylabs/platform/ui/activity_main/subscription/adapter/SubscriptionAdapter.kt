package com.owlylabs.platform.ui.activity_main.subscription.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.databinding.FragmentStartScreenSubscriptionSubscriptionItemBinding

class SubscriptionAdapter(
    private val subscriptions: List<Pair<String, String>>,
    private val onItemClick: (Pair<String, String>) -> Unit
): RecyclerView.Adapter<SubscriptionAdapter.SubscriptionHolder>() {

    private var selectedItemPosition = 0
    private var isFirstLaunch = true

    inner class SubscriptionHolder(private val binding: FragmentStartScreenSubscriptionSubscriptionItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(subscription: Pair<String, String>, isSelected: Boolean) {
            binding.textViewTitle.text = subscription.first
            binding.textViewPrice.text = subscription.second
            itemView.isSelected = isSelected
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriptionHolder {
        val binding = FragmentStartScreenSubscriptionSubscriptionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscriptionHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscriptionHolder, position: Int) {
        holder.bind(
            subscription = subscriptions[position],
            isSelected = position == selectedItemPosition
        )
        if (isFirstLaunch) {
            when(position) {
                0 -> {
                    onItemClick.invoke(subscriptions[0])
                    isFirstLaunch = false
                }
            }
        }
        holder.itemView.setOnClickListener {
            val previousSelectedItemPosition = selectedItemPosition
            selectedItemPosition = holder.adapterPosition
            notifyItemChanged(selectedItemPosition)
            notifyItemChanged(previousSelectedItemPosition)
            onItemClick.invoke(subscriptions[position])
        }
    }

    override fun getItemCount(): Int = subscriptions.size
}