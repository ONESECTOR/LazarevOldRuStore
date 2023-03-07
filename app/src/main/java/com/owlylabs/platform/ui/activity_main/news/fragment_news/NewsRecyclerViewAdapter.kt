package com.owlylabs.platform.ui.news.fragment_news

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.owlylabs.platform.R
import com.owlylabs.platform.databinding.FragmentNewsListViewHolderBinding
import com.owlylabs.platform.model.data.NewsData
import com.owlylabs.platform.model.data.SectionData

class NewsRecyclerViewAdapter(val iNavToNewsDetail: INavToNewsDetail) : RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder>() {

    private var data = ArrayList<NewsData>()
    lateinit var sectionData: SectionData
    lateinit var diffUtilCallback: NewsRecyclerViewDiffUtilCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.fragment_news_list_view_holder,
                parent,
                false),
            parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.binding.newsData = data.get(position)
        holder.binding.executePendingBindings()
    }

    inner class NewsViewHolder(val binding: FragmentNewsListViewHolderBinding, parent: ViewGroup) : RecyclerView.ViewHolder(binding.root) {

        var img : ImageView private set
        var title : TextView private set
        var description : TextView private set
        var actionButton : TextView private set

        init {
            binding.root.setOnClickListener {
                iNavToNewsDetail.navToNewsDetail(data.get(adapterPosition).id.toInt())
            }
            img = binding.root.findViewById(R.id.img)
            title = binding.root.findViewById(R.id.title)
            description = binding.root.findViewById(R.id.description)
            actionButton = binding.root.findViewById(R.id.action_button)

            sectionData.coverRatioWtoH?.let { wToHRatio ->
                var width = parent.measuredWidth
                width = width - binding.root.paddingStart - binding.root.paddingEnd
                //val llLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, binding.root)
                img.layoutParams.height = (width / wToHRatio).toInt()
            }

        }
    }

    fun updateData(newData : List<NewsData>, isNeedFullReloadRecyclerView : Boolean){
        if (!::diffUtilCallback.isInitialized) {
            diffUtilCallback = NewsRecyclerViewDiffUtilCallback()
        }
        diffUtilCallback.update(data, newData)
        val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
        data.clear()
        data.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
       /* if (isNeedFullReloadRecyclerView){
            data.clear()
            data.addAll(newData)
            notifyDataSetChanged()
        } else {
            if (!::diffUtilCallback.isInitialized) {
                diffUtilCallback = NewsRecyclerViewDiffUtilCallback()
            }
            diffUtilCallback.update(data, newData)
            val diffResult = DiffUtil.calculateDiff(diffUtilCallback, false)
            data.clear()
            data.addAll(newData)
            diffResult.dispatchUpdatesTo(this)
        }*/
    }

    interface INavToNewsDetail{
        fun navToNewsDetail(detailId : Int)
    }
}