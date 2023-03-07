package com.owlylabs.platform.ui.tab_with_sections.childrens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.util.MeasureUtil
import kotlin.properties.Delegates

class HorizontalListAudioRecyclerViewAdapter(val INavToFragmentByDeepLink: INavToFragmentByDeepLink) : RecyclerView.Adapter<HorizontalListAudioRecyclerViewAdapter.HorizontalListItem>() {
    private val data = ArrayList<AudioData>()
    private lateinit var sectionData: SectionData
    private var parentWidth: Int by Delegates.notNull<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalListItem {
        return HorizontalListItem(LayoutInflater
            .from(parent.context)
            .inflate(R.layout.fragment_tab_that_represents_section_horizontal_list_item_audio, parent, false), parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HorizontalListItem, position: Int) {
        holder.bind()
        Glide.with(holder.imageView)
            .load(ApiConstants.API_BASE_URL.plus(data.get(position).urlImg))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)
    }

    inner class HorizontalListItem(itemView: View, val parent: ViewGroup):
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener{
        val imageView: ImageView

        init {
            sectionData.maxWidthDP?.let {
                itemView.layoutParams.width = FrameworkUtil.convertDpToPixels(it.toFloat(), itemView.context ).toInt()
            }
            imageView = itemView.findViewById(R.id.imageView)
            itemView.setOnClickListener(this)
        }

        fun bind(){
            val heightRatio = sectionData.coverRatioWtoH
            val itemViewWidth = MeasureUtil.getWidthOfHorizontalBookItem(itemView.context, parentWidth)

            if (heightRatio != null){
                val lp = FrameLayout.LayoutParams(itemViewWidth, (itemViewWidth/heightRatio).toInt())
                itemView.layoutParams = lp
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createAudioDetailDeepLink(data.get(adapterPosition).idBook.toInt())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    fun updateData(data: List<AudioData>, sectionData: SectionData, parentWidth: Int){
        this.parentWidth = parentWidth
        this.sectionData = sectionData
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}