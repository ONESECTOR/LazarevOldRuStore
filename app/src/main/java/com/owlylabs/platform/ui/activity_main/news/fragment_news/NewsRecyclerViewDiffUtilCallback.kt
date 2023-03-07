package com.owlylabs.platform.ui.news.fragment_news

import androidx.recyclerview.widget.DiffUtil
import com.owlylabs.platform.model.data.NewsData

class NewsRecyclerViewDiffUtilCallback : DiffUtil.Callback() {

        var oldData = ArrayList<NewsData>()
        var newData = ArrayList<NewsData>()

        override fun getOldListSize(): Int {
                return oldData.size
            }
        override fun getNewListSize(): Int {
                return newData.size
            }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return oldData[oldItemPosition].id == newData[newItemPosition].id
            }


        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return (oldData[oldItemPosition].url_img == newData[newItemPosition].url_img) &&
                                (oldData[oldItemPosition].description == newData[newItemPosition].description) &&
                                (oldData[oldItemPosition].title == newData[newItemPosition].title) &&
                                (oldData[oldItemPosition].button_title == newData[newItemPosition].button_title)
            }

        fun update(oldItems: List<NewsData>, newItems: List<NewsData>) {
                oldData.clear()
                oldData.addAll(oldItems)
                newData.clear()
                newData.addAll(newItems)
            }
    }