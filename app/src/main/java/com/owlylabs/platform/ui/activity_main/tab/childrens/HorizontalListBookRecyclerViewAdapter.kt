package com.owlylabs.platform.ui.tab_with_sections.childrens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.github.chuross.rxobservablefield.extension.toObservableField
import com.owlylabs.epublibrary.OwlyReader
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.databinding.FragmentTabThatRepresentsSectionHorizontalListItemBookBinding
import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.ui.tab_with_sections.BookObservableProgressHelperClass
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.util.MeasureUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlin.properties.Delegates

class HorizontalListBookRecyclerViewAdapter(val INavToFragmentByDeepLink: INavToFragmentByDeepLink) : RecyclerView.Adapter<HorizontalListBookRecyclerViewAdapter.HorizontalListItem>() {
    private val data = ArrayList<BookData>()
    private lateinit var sectionData: SectionData
    private var parentWidth: Int by Delegates.notNull<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalListItem {
        return HorizontalListItem(
            FragmentTabThatRepresentsSectionHorizontalListItemBookBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HorizontalListItem, position: Int) {
        Glide
            .with(holder.imageView)
            .load(ApiConstants.API_BASE_URL.plus(data.get(position).urlImg))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(holder.imageView)

        holder.compositeDisposable.dispose()
        holder.compositeDisposable = CompositeDisposable()

        val bookObservableProgressHelperClass = BookObservableProgressHelperClass(
            OwlyReader.getBookProgressFlowable(data.get(position).idBook.toInt())
                .toObservable()
                .map {
                    holder.itemView.context.getString(R.string.placeholder_progress_procent_placeholder, it)
                }
                .toObservableField<String>(holder.compositeDisposable))
        holder.binding.observableProgress = bookObservableProgressHelperClass
        holder.binding.executePendingBindings()
    }

    inner class HorizontalListItem(val binding: FragmentTabThatRepresentsSectionHorizontalListItemBookBinding, val parent: ViewGroup):
        RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        val imageView: ImageView
        val parentLayout: LinearLayout
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        var compositeDisposable: CompositeDisposable = CompositeDisposable()
        var progressFlowableDisposable: Disposable? = null

        init {
            itemView.setOnClickListener(this)
            imageView = itemView.findViewById(R.id.imageView)
            parentLayout = itemView.findViewById(R.id.parentLayout)

            val heightRatio = sectionData.coverRatioWtoH
            val itemViewWidth = MeasureUtil.getWidthOfHorizontalBookItem(itemView.context, parentWidth)

            if (heightRatio != null){
                val lpImage = LinearLayout.LayoutParams(itemViewWidth, (itemViewWidth/heightRatio).toInt())
                cardView.layoutParams = lpImage

                val lp = RecyclerView.LayoutParams(itemViewWidth, RecyclerView.LayoutParams.WRAP_CONTENT)
                parentLayout.layoutParams = lp
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createBookDetailDeepLink(data.get(adapterPosition).idBook.toInt())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    override fun onViewRecycled(holder: HorizontalListItem) {
        super.onViewRecycled(holder)
        holder.progressFlowableDisposable?.dispose()
    }

    fun updateData(data: List<BookData>, sectionData: SectionData, parentWidth: Int){
        this.parentWidth = parentWidth
        this.sectionData = sectionData
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}