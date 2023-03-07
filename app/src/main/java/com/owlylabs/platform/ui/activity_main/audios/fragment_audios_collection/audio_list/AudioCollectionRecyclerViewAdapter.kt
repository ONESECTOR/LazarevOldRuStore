package com.owlylabs.platform.ui.audios.fragment_audios_collection.audio_list

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.constants.AppLogicConstants


class AudioCollectionRecyclerViewAdapter(val INavToFragmentByDeepLink: INavToFragmentByDeepLink) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<AudioCollectionListItemAbstract>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            AppLogicConstants.AudioCollectionViewHolderType.SUBTITLE.ordinal -> {
                return SubTitleHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_book_collection_list_item_subtitle,
                            parent,
                            false
                        )
                )
            }
            AppLogicConstants.AudioCollectionViewHolderType.AUDIO.ordinal -> {
                return AudioHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_audio_collection_list_item_audio,
                            parent,
                            false
                        ), parent
                )
            }
            else -> {
                throw IllegalArgumentException("HolderNotFound")
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return data.get(position).getItemType()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = data.get(position).getItemData()
        when(holder) {
            is SubTitleHolder -> {
                if (itemData is AudioCollectionListItemSubtitle) {
                    itemData.data.subTitle?.let {
                        holder.subTitleTextView.text = it
                    }

                    itemData.data.backgroundColor?.let {
                        holder.subTitleTextView.setBackgroundColor(Color.parseColor(it))
                    } ?: run {
                        holder.subTitleTextView.background = null
                    }
                }
            }
            is AudioHolder -> {
                holder.bind()
                if (itemData is AudioCollectionListItemSingleAudio) {
                    Glide
                        .with(holder.itemView)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.data.urlImg))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.imageView)
                    holder.bookName.text = itemData.data.nameBook
                    holder.audiobook_length.text = itemData.data.audioDuration
                }
            }
        }
    }

    inner class SubTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subTitleTextView: TextView

        init {
            subTitleTextView = itemView.findViewById(R.id.sub_title)
        }
    }

    inner class AudioHolder(itemView: View, val parent: ViewGroup) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val audiobook_length: TextView = itemView.findViewById(R.id.audiobook_length)
        val bookName: TextView = itemView.findViewById(R.id.bookName)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(){
            val currentSection = data.get(adapterPosition).sectionData
            val heightRatio = currentSection.coverRatioWtoH
            //val itemViewWidth = MeasureUtil.getWidthOfItem(parent.context, parent, data.get(adapterPosition).getItemType())

            if (heightRatio != null){
                (cardView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "W, ".plus(heightRatio).plus(":1")
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createAudioDetailDeepLink(data.get(adapterPosition).getInnerDataId())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    fun updateData(newData: ArrayList<AudioCollectionListItemAbstract>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}