package com.owlylabs.platform.ui.books.fragment_books_collection

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
import com.github.chuross.rxobservablefield.extension.toObservableField
import com.owlylabs.epublibrary.OwlyReader
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.databinding.FragmentBookCollectionListItemBookBinding
import com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract.BooksCollectionListItemAbstract
import com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract.BooksCollectionListItemSingleBook
import com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract.BooksCollectionListItemSubtitle
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.util.MeasureUtil
import com.owlylabs.platform.constants.AppLogicConstants
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


class BooksCollectionRecyclerViewAdapter(val INavToFragmentByDeepLink: INavToFragmentByDeepLink) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = ArrayList<BooksCollectionListItemAbstract>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when(viewType){
            AppLogicConstants.BookCollectionViewHolderType.SUBTITLE.ordinal -> {
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
            AppLogicConstants.BookCollectionViewHolderType.BOOK.ordinal -> {
                return BookHolder(
                    FragmentBookCollectionListItemBookBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    parent
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
                if (itemData is BooksCollectionListItemSubtitle) {
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
            is BookHolder -> {
                holder.bind()
                if (itemData is BooksCollectionListItemSingleBook) {
                    Glide
                        .with(holder.itemView)
                        .load(ApiConstants.API_BASE_URL.plus(itemData.data.urlImg))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(holder.imageView)

                    holder.bookName.text = itemData.data.nameBook
                    holder.number_of_pages.text = holder.itemView.context.getString(R.string.placeholder_number_of_pages, itemData.data.countPage)

                    holder.compositeDisposable.dispose()
                    holder.compositeDisposable = CompositeDisposable()

                    val bookObservableProgressHelperClass = BookCollectionObservableProgressHelperClass(
                        OwlyReader.getBookProgressFlowable(itemData.data.idBook.toInt())
                            .toObservable()
                            .toObservableField<Int>(holder.compositeDisposable))
                    holder.binding.observableProgress = bookObservableProgressHelperClass
                    holder.binding.executePendingBindings()
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

    inner class BookHolder(val binding: FragmentBookCollectionListItemBookBinding, val parent: ViewGroup) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        val number_of_pages: TextView = itemView.findViewById(R.id.number_of_pages)
        val bookName: TextView = itemView.findViewById(R.id.bookName)

        var compositeDisposable: CompositeDisposable = CompositeDisposable()
        var progressFlowableDisposable: Disposable? = null

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(){
            val currentSection = data.get(adapterPosition).sectionData
            val heightRatio = currentSection.coverRatioWtoH
            val itemViewWidth = MeasureUtil.getWidthOfItem(parent.context, parent, data.get(adapterPosition).getItemType())

            if (heightRatio != null){
                //val lp = ConstraintLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (itemViewWidth/heightRatio).toInt())
                (cardView.layoutParams as ConstraintLayout.LayoutParams).dimensionRatio = "W, ".plus(heightRatio).plus(":1")
            }
        }

        override fun onClick(v: View?) {
            val uri = DeepLinkUtil.createBookDetailDeepLink(data.get(adapterPosition).getInnerDataId())
            INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
        }
    }

    override fun onViewRecycled(holder: RecyclerView.ViewHolder) {
        super.onViewRecycled(holder)
        if (holder is BookHolder){
            holder.progressFlowableDisposable?.dispose()
        }
    }

    fun updateData(newData: ArrayList<BooksCollectionListItemAbstract>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}