package com.owlylabs.platform.ui.tab_with_sections

import android.graphics.Color
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.chuross.rxobservablefield.extension.toObservableField
import com.owlylabs.epublibrary.OwlyReader
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.databinding.FragmentTabThatRepresentsSectionSingleBookBinding
import com.owlylabs.platform.model.data.tab_list_item_types.*
import com.owlylabs.platform.model.data.tab_list_item_types.audio.TabListItemHorizontalAudioList
import com.owlylabs.platform.model.data.tab_list_item_types.audio.TabListItemSingleAudio
import com.owlylabs.platform.model.data.tab_list_item_types.book.TabListItemHorizontalBookList
import com.owlylabs.platform.model.data.tab_list_item_types.book.TabListItemSingleBook
import com.owlylabs.platform.ui.tab_with_sections.childrens.HorizontalListAudioRecyclerViewAdapter
import com.owlylabs.platform.ui.tab_with_sections.childrens.HorizontalListBannerRecyclerViewAdapter
import com.owlylabs.platform.ui.tab_with_sections.childrens.HorizontalListBookRecyclerViewAdapter
import com.owlylabs.platform.ui.tab_with_sections.childrens.HorizontalListRecyclerViewItemDecorations
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.util.MeasureUtil
import com.owlylabs.platform.constants.AppLogicConstants
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class TabThatRepresentsSectionsRecyclerViewAdapter(val INavToFragmentByDeepLink: INavToFragmentByDeepLink, val interfaceBannerClick: InterfaceBannerClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<TabListItemAbstract>()

    private val scrollStates = hashMapOf<String, Parcelable?>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            AppLogicConstants.TabViewHolderType.TITLE.ordinal -> {
                return TitleHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_title,
                            parent,
                            false
                        )
                )
            }
            AppLogicConstants.TabViewHolderType.SUBTITLE.ordinal -> {
                return SubTitleHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_subtitle,
                            parent,
                            false
                        )
                )
            }
            AppLogicConstants.TabViewHolderType.BOOK.ordinal -> {
                return BookHolder(
                    FragmentTabThatRepresentsSectionSingleBookBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent
                )
            }
            AppLogicConstants.TabViewHolderType.BOOK_HORIZONTAL_SECTION.ordinal -> {
                return BookHorizontalListHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_horizontal_list_with_title_and_subtitle,
                            parent,
                            false
                        ), parent
                )
            }
            AppLogicConstants.TabViewHolderType.AUDIO.ordinal -> {
                return AudioHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_single_audio,
                            parent,
                            false
                        ), parent
                )
            }
            AppLogicConstants.TabViewHolderType.AUDIO_HORIZONTAL_SECTION.ordinal -> {
                return AudioHorizontalListHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_horizontal_list_with_title_and_subtitle,
                            parent,
                            false
                        ),
                    parent
                )
            }
            AppLogicConstants.TabViewHolderType.SEPARATOR.ordinal -> {
                return SeparatorHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_separator,
                            parent,
                            false
                        )
                )
            }
            AppLogicConstants.TabViewHolderType.BANNER_HORIZONTAL_SECTION.ordinal -> {
                return BannerHorizontalListHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_horizontal_list_banners,
                            parent,
                            false
                        ), parent
                )
            }
            AppLogicConstants.TabViewHolderType.VIDEO.ordinal -> {
                return VideoHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_single_video,
                            parent,
                            false
                        )
                , parent)
            }
            AppLogicConstants.TabViewHolderType.NEWS_ITEM.ordinal -> {
                return NewsHolder(
                    LayoutInflater
                        .from(parent.context)
                        .inflate(
                            R.layout.fragment_tab_that_represents_section_single_news,
                            parent,
                            false
                        )
                , parent)
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
            is TitleHolder -> {
                if (itemData is TabListItemTitle) {
                    itemData.sectionData.title?.let {
                        holder.titleTextView.text = it
                    }

                    when (itemData.sectionData.showAdditionalAction) {
                        1 -> holder.titleTextView.setCompoundDrawablesWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.vector_forward,
                            0
                        )
                        else -> holder.titleTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                    }
                }
            }
            is SubTitleHolder -> {
                if (itemData is TabListItemSubtitle) {
                    itemData.sectionData.subTitle?.let {
                        holder.subTitleTextView.text = it
                    }
                }
            }
            is BookHolder -> {
                holder.bind()
                if (itemData is TabListItemSingleBook) {
                    Glide
                        .with(holder.itemView)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.data.urlImg))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.imageView)

                    holder.compositeDisposable.dispose()
                    holder.compositeDisposable = CompositeDisposable()

                    val bookObservableProgressHelperClass = BookObservableProgressHelperClass(OwlyReader.getBookProgressFlowable(itemData.data.idBook.toInt())
                        .toObservable()
                        .map {
                            //String.format()
                            holder.itemView.context.getString(R.string.placeholder_progress_procent_placeholder, it)
                        }
                        .toObservableField<String>(holder.compositeDisposable))
                    holder.binding.observableProgress = bookObservableProgressHelperClass
                    holder.binding.executePendingBindings()
                }
            }
            is BookHorizontalListHolder -> {
                if (itemData is TabListItemHorizontalBookList) {
                    holder.bookListTitle.text = itemData.sectionData.title
                    itemData.sectionData.subTitle?.let {
                        holder.bookListSubtitle.text = it
                        holder.bookListSubtitle.visibility = View.VISIBLE
                    } ?: run {
                        holder.bookListSubtitle.visibility = View.GONE
                    }
                    holder.recyclerViewAdapter.updateData(itemData.data, itemData.sectionData, holder.parentView.width)


                    val state = scrollStates[itemData.sectionData.title]
                    if(state != null){
                        holder.layoutManager.onRestoreInstanceState(state)
                    }else{
                        holder.layoutManager.scrollToPosition(0)
                    }

                    when (itemData.sectionData.showAdditionalAction) {
                        1 -> {
                            holder.bookListTitle.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.vector_forward,
                                0
                            )
                            holder.linearLayout.setOnClickListener {
                                val uri = DeepLinkUtil.createBookCollectionlDeepLink(data.get(position).getInnerDataId())
                                INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
                            }
                        }
                        else -> {
                            holder.bookListTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            holder.linearLayout.setOnClickListener(null)
                        }
                    }

                    itemData.sectionData.backgroundColor?.let {
                        holder.itemView.setBackgroundColor(Color.parseColor(it))
                    } ?: run {
                        holder.itemView.background = null
                    }
                }
            }
            is AudioHolder -> {
                holder.bind()
                if (itemData is TabListItemSingleAudio) {
                    Glide
                        .with(holder.itemView)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.data.urlImg))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.imageView)
                }
            }
            is AudioHorizontalListHolder -> {
                if (itemData is TabListItemHorizontalAudioList) {
                    holder.audioListTitle.text = itemData.sectionData.title
                    itemData.sectionData.subTitle?.let {
                        holder.audioListSubtitle.text = it
                        holder.audioListSubtitle.visibility = View.VISIBLE
                    } ?: run {
                        holder.audioListSubtitle.visibility = View.GONE
                    }
                    holder.recyclerViewAdapter.updateData(itemData.audioList, itemData.sectionData, holder.parent.width)


                    val state = scrollStates[itemData.sectionData.title]
                    if(state != null){
                        holder.layoutManager.onRestoreInstanceState(state)
                    }else{
                        // We don't have a state for this VH yet,
                        // so reset its scroll position
                        holder.layoutManager.scrollToPosition(0)
                    }

                    when (itemData.sectionData.showAdditionalAction) {
                        1 -> {
                            holder.audioListTitle.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.vector_forward,
                                0
                            )
                            holder.parentLayout.setOnClickListener {
                                val uri = DeepLinkUtil.createAudioCollectionlDeepLink(data.get(position).getInnerDataId())
                                INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
                            }
                        }
                        else -> {
                            holder.audioListTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            holder.parentLayout.setOnClickListener(null)
                        }
                    }

                    itemData.sectionData.backgroundColor?.let {
                        holder.itemView.setBackgroundColor(Color.parseColor(it))
                    } ?: run {
                        holder.itemView.background = null
                    }
                }
            }
            is SeparatorHolder -> {
                if (itemData is TabListItemSeparator) {
                    itemData.sectionData.lineColor?.let{
                        holder.itemView.setBackgroundColor(Color.parseColor(it))
                    }
                }
            }
            is BannerHorizontalListHolder -> {
                if (itemData is TabListItemHorizontalBannerList) {
                    holder.recyclerViewAdapter.updateData(itemData.data, itemData.sectionData, holder.parentView)
                }
            }
            is VideoHolder -> {
                holder.bind()
                if (itemData is TabListItemSingleVideo) {
                    Glide
                        .with(holder.itemView)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.data.url_img))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.imageView)

                    holder.duration.text = itemData.data.video_duration
                }
            }
            is NewsHolder -> {
                if (itemData is TabListItemSingleNews) {
                    holder.bind()
                    Glide
                        .with(holder.itemView)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.data.url_img))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .transform(CenterCrop(), RoundedCorners(FrameworkUtil.getDimenstionInPixels(holder.itemView.context, R.dimen.banner_list_item_horizontal_item_radius )))
                        .into(holder.imageView)
                    holder.title.text = itemData.data.title
                    if (!itemData.data.description.isEmpty()){
                        holder.description.text = itemData.data.description
                    }
                    if (!itemData.data.button_title.isEmpty()){
                        holder.actionButton.text = itemData.data.button_title
                        holder.actionButton.visibility = View.VISIBLE
                    } else {
                        holder.actionButton.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is BookHorizontalListHolder){
            val innerData = data[holder.position].getItemData()
            if (innerData is TabListItemHorizontalBookList){
                innerData.sectionData.title?.let {
                    scrollStates[it] = holder.layoutManager.onSaveInstanceState()
                }
            }
        }
        if (holder is BookHolder){
            val innerData = data[holder.position].getItemData()
            if (innerData is TabListItemSingleBook){
                holder.progressFlowableDisposable?.dispose()
            }
        }
    }

    inner class TitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView

        init {
            titleTextView = itemView.findViewById(R.id.title)
        }
    }

    inner class SubTitleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subTitleTextView: TextView

        init {
            subTitleTextView = itemView.findViewById(R.id.sub_title)
        }
    }

    inner class BookHolder(val binding: FragmentTabThatRepresentsSectionSingleBookBinding, val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val imageView: ImageView
        val cardView: CardView
        val progress: TextView

        var compositeDisposable: CompositeDisposable = CompositeDisposable()
        var progressFlowableDisposable:Disposable? = null

        init {
            imageView = itemView.findViewById(R.id.imageView)
            cardView = itemView.findViewById(R.id.cardView)
            progress = itemView.findViewById(R.id.progress)

            itemView.setOnClickListener(this)
        }

        fun bind(){
            val currentSection = data.get(adapterPosition).sectionData
            val heightRatio = currentSection.coverRatioWtoH
            //val itemViewWidth = MeasureUtil.getWidthOfItem(parent.context, parent, data.get(adapterPosition))
            val itemViewWidth = MeasureUtil.getWidthOfHorizontalBookItem(parent.context, parent.width)

            if (heightRatio != null){
                val rootLP = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (itemViewWidth/heightRatio).toInt())
                cardView.layoutParams = rootLP
                //val lp = FrameLayout.LayoutParams(itemViewWidth, (itemViewWidth/heightRatio).toInt())
                //imageView.layoutParams = lp
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createBookDetailDeepLink(data.get(adapterPosition).getInnerDataId())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    inner class AudioHolder(itemView: View, val parent: ViewGroup) : RecyclerView.ViewHolder(itemView), View.OnClickListener  {
        val imageView: ImageView
        val cardView: CardView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            cardView = itemView.findViewById(R.id.cardView)
            itemView.setOnClickListener(this)
        }

        fun bind(){
            val currentSection = data.get(adapterPosition).sectionData
            val heightRatio = currentSection.coverRatioWtoH
            val itemViewWidth = MeasureUtil.getWidthOfItem(parent.context, parent, data.get(adapterPosition))

            if (heightRatio != null){
                val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (itemViewWidth/heightRatio).toInt())
                imageView.layoutParams = lp
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createAudioDetailDeepLink(data.get(adapterPosition).getInnerDataId())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    inner class BookHorizontalListHolder(itemView: View, val parentView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val bookListTitle: TextView
        val bookListSubtitle: TextView
        val bookListHorizontalList: RecyclerView
        val recyclerViewAdapter =
            HorizontalListBookRecyclerViewAdapter(INavToFragmentByDeepLink)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        val linearLayout: ViewGroup

        init {
            bookListTitle = itemView.findViewById(R.id.title)
            bookListSubtitle = itemView.findViewById(R.id.sub_title)
            bookListHorizontalList = itemView.findViewById(R.id.horizontal_list)
            bookListHorizontalList.layoutManager = layoutManager
            bookListHorizontalList.adapter = recyclerViewAdapter
            bookListHorizontalList.addItemDecoration(HorizontalListRecyclerViewItemDecorations(itemView.context))
            linearLayout = itemView.findViewById(R.id.parentLayout)
        }
    }

    inner class AudioHorizontalListHolder(itemView: View, val parent: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val audioListTitle: TextView
        val audioListSubtitle: TextView
        val audioListHorizontalList: RecyclerView
        val recyclerViewAdapter =
            HorizontalListAudioRecyclerViewAdapter(INavToFragmentByDeepLink)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        val parentLayout: ViewGroup

        init {
            audioListTitle = itemView.findViewById(R.id.title)
            audioListSubtitle = itemView.findViewById(R.id.sub_title)
            audioListHorizontalList = itemView.findViewById(R.id.horizontal_list)
            audioListHorizontalList.layoutManager = layoutManager
            audioListHorizontalList.adapter = recyclerViewAdapter
            audioListHorizontalList.addItemDecoration(HorizontalListRecyclerViewItemDecorations(itemView.context))
            parentLayout = itemView.findViewById(R.id.parentLayout)
        }
    }

    inner class SeparatorHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    inner class BannerHorizontalListHolder(itemView: View, val parentView: ViewGroup) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView
        val recyclerViewAdapter =
            HorizontalListBannerRecyclerViewAdapter(interfaceBannerClick)
        val layoutManager = LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)

        init {
            recyclerView = itemView.findViewById(R.id.recycler_view)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = recyclerViewAdapter
            recyclerView.addItemDecoration(HorizontalListRecyclerViewItemDecorations(itemView.context))
        }
    }

    inner class VideoHolder(itemView: View, val parent: ViewGroup) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val imageView: ImageView
        val cardView: CardView
        val duration: TextView

        init {
            imageView = itemView.findViewById(R.id.imageView)
            cardView = itemView.findViewById(R.id.cardView)
            duration = itemView.findViewById(R.id.duration)
            itemView.setOnClickListener(this)
        }

        fun bind(){
            val currentSection = data.get(adapterPosition).sectionData
            val heightRatio = currentSection.coverRatioWtoH
            val itemViewWidth = MeasureUtil.getWidthOfItem(parent.context, parent, data.get(adapterPosition))

            if (heightRatio != null){
                val height = (itemViewWidth/heightRatio).toInt()
                val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
                cardView.layoutParams = lp
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createVideoDetailDeepLink(data.get(adapterPosition).getInnerDataId())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    inner class NewsHolder(itemView: View, val parent: ViewGroup) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var imageView: ImageView private set
        var title : TextView private set
        var description : TextView private set
        var actionButton : TextView private set
        //var cardView: MaterialCardView private set

        init {
            imageView = itemView.findViewById(R.id.imageView)
            title = itemView.findViewById(R.id.title)
            description = itemView.findViewById(R.id.description)
            actionButton = itemView.findViewById(R.id.action_button)
            //cardView = itemView.findViewById(R.id.cardView)
            itemView.setOnClickListener(this)

            //val lp = imageView.layoutParams as ConstraintLayout.LayoutParams
            //lp.dimensionRatio = "1.61".plus(":1")
            //lp.width = itemViewWidth

            //val lp = imageView.layoutParams as PercentRelativeLayout.LayoutParams
            //lp.percentLayoutInfo.heightPercent

        }

        fun bind(){
            val currentSection = data.get(adapterPosition).sectionData
            val heightRatio = currentSection.coverRatioWtoH
            val itemViewWidth = MeasureUtil.getWidthOfItem(parent.context, parent, data.get(adapterPosition))

            if (heightRatio != null){
                 val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (itemViewWidth/heightRatio).toInt())
                imageView.layoutParams = lp
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createNewsDetailDeepLink(data.get(adapterPosition).getInnerDataId())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }




    fun updateData(newData: ArrayList<TabListItemAbstract>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}