package com.owlylabs.platform.ui.tab_with_sections.childrens

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Handler
import android.os.HandlerThread
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.data.BannerData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.ui.tab_with_sections.InterfaceBannerClick
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.util.MeasureUtil
import java.lang.NullPointerException
import kotlin.properties.Delegates


class HorizontalListBannerRecyclerViewAdapter(val interfaceBannerClick: InterfaceBannerClick) :
    RecyclerView.Adapter<HorizontalListBannerRecyclerViewAdapter.HorizontalListItem>() {
    private var parentWidth: Int by Delegates.notNull()
    private val data = ArrayList<BannerData>()
    private lateinit var sectionData: SectionData
    private var textBlockHeight: Int by Delegates.notNull()

    private var merriweather: Typeface? = null
    private var lato: Typeface? = null

    private var mHandler: Handler? = null
    private var mHandlerLato: Handler? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorizontalListItem {
        return HorizontalListItem(
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.fragment_tab_that_represents_section_horizontal_list_item_banner,
                    parent,
                    false
                )
            , parent
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HorizontalListItem, position: Int) {
        holder.bind()
        Glide
            .with(holder.imageView)
            .load(ApiConstants.API_BASE_URL.plus(data.get(position).url_img))
            .transition(DrawableTransitionOptions.withCrossFade())
            .transform(
                CenterCrop(),
                RoundedCorners(
                    FrameworkUtil.getDimenstionInPixels(
                        holder.itemView.context,
                        R.dimen.banner_list_item_horizontal_item_radius
                    )
                )
            )
            .into(holder.imageView)
        holder.title.text = data.get(position).title
        holder.subTitle.text = data.get(position).description
        if (data.get(position).button_title.isEmpty()) {
            holder.actionText.visibility = View.INVISIBLE
        } else {
            holder.actionText.text = data.get(position).button_title
            holder.actionText.visibility = View.VISIBLE
        }
    }

    inner class HorizontalListItem(itemView: View, val parent: ViewGroup) :
        RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val title: TextView = itemView.findViewById(R.id.title)
        val subTitle: TextView = itemView.findViewById(R.id.sub_title)
        val actionText: TextView = itemView.findViewById(R.id.action_text)
        val text_block: ConstraintLayout = itemView.findViewById(R.id.text_block)


        init {
            sectionData.maxWidthDP?.let {
                //itemView.layoutParams.width = FrameworkUtil.convertDpToPixels(it.toFloat(), itemView.context ).toInt()
            }
            itemView.setOnClickListener(this)
        }

        fun bind() {
            merriweather?.let {
                title.typeface = it
            }
            lato?.let {
                subTitle.typeface = it
                actionText.typeface = it
            }
            val heightRatio = sectionData.coverRatioWtoH

            val itemViewWidth =
                MeasureUtil.getWidthOfHorizontalBannerItem(itemView.context, parentWidth)

            if (heightRatio != null) {
                val height = (itemViewWidth / heightRatio).toInt()

                val lpItemView =
                    RecyclerView.LayoutParams(itemViewWidth, RecyclerView.LayoutParams.WRAP_CONTENT)
                itemView.layoutParams = lpItemView

                val lpTextBlock = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    textBlockHeight
                )
                text_block.layoutParams = lpTextBlock

                val lpImageView =
                    LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
                imageView.layoutParams = lpImageView

                //text_block.requestLayout()

            }
        }

        override fun onClick(v: View?) {
            interfaceBannerClick.onBannerClick(data.get(adapterPosition))
        }
    }

    fun updateData(data: List<BannerData>, sectionData: SectionData, parent: ViewGroup) {
        this.parentWidth = parent.width

        val start = System.nanoTime()
        var finalHeight = 0
        val myTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
        myTextPaint.typeface = Typeface.SANS_SERIF

        data.forEach {
            var tempHeight = 0
            if (it.title.isNotEmpty()) {
                myTextPaint.textSize =
                    parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_title_size)
                myTextPaint.letterSpacing = 0f
                val builder =
                    StaticLayout.Builder.obtain(
                        it.title,
                        0,
                        it.title.length,
                        myTextPaint,
                        MeasureUtil.getWidthOfHorizontalBannerItem(parent.context, parentWidth)
                    )
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .setIncludePad(true)
                val myStaticLayout = builder.build()
                tempHeight = tempHeight + myStaticLayout.height
                tempHeight = tempHeight + FrameworkUtil.getDimenstionInPixels(
                    parent.context,
                    R.dimen.banner_list_item_horizontal_item_title_top_margin
                )
            }
            if (it.description.isNotEmpty()) {
                myTextPaint.textSize =
                    parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_subtitle_size)
                myTextPaint.letterSpacing = ResourcesCompat.getFloat(
                    parent.resources,
                    R.dimen.banner_list_item_horizontal_item_letter_spacing
                )
                val builder =
                    StaticLayout.Builder.obtain(
                        it.description,
                        0,
                        it.description.length,
                        myTextPaint,
                        MeasureUtil.getWidthOfHorizontalBannerItem(parent.context, parentWidth)
                    )
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .setLineSpacing(
                            parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_line_spacing_extra),
                            1.0f
                        )
                        .setIncludePad(true)
                val myStaticLayout = builder.build()
                tempHeight = tempHeight + myStaticLayout.height
                tempHeight = tempHeight + FrameworkUtil.getDimenstionInPixels(
                    parent.context,
                    R.dimen.banner_list_item_horizontal_item_subtitle_top_margin
                )
            }
            if (it.button_title.isNotEmpty()) {
                myTextPaint.textSize =
                    parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_action_button_size)
                myTextPaint.letterSpacing = ResourcesCompat.getFloat(
                    parent.resources,
                    R.dimen.banner_list_item_horizontal_item_letter_spacing
                )
                val builder =
                    StaticLayout.Builder.obtain(
                        it.button_title,
                        0,
                        it.button_title.length,
                        myTextPaint,
                        MeasureUtil.getWidthOfHorizontalBannerItem(parent.context, parentWidth)
                    )
                        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                        .setLineSpacing(
                            parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_line_spacing_extra),
                            1.0f
                        )
                        .setIncludePad(true)
                val myStaticLayout = builder.build()
                tempHeight = tempHeight + myStaticLayout.height
                tempHeight = tempHeight + FrameworkUtil.getDimenstionInPixels(
                    parent.context,
                    R.dimen.banner_list_item_horizontal_item_action_button_top_margin
                )
            }
            if (tempHeight > finalHeight) {
                finalHeight = tempHeight
            }
        }

        System.out.println("timeis".plus(System.nanoTime() - start))

        textBlockHeight = finalHeight

        this.sectionData = sectionData
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()

        //loadFontsAndReloadViewHolders(parent)
    }

    private fun loadFontsAndReloadViewHolders(parent: ViewGroup) {
        var query = "name=Merriweather&amp;weight=700"

        val request = FontRequest(
            "com.google.android.gms.fonts",
            "com.google.android.gms",
            query,
            R.array.com_google_android_gms_fonts_certs
        )

        val callback: FontsContractCompat.FontRequestCallback =
            object : FontsContractCompat.FontRequestCallback() {
                override fun onTypefaceRetrieved(typeface: Typeface) {
                    merriweather = typeface

                    query = "Lato"

                    val requestLato = FontRequest(
                        "com.google.android.gms.fonts",
                        "com.google.android.gms",
                        query,
                        R.array.com_google_android_gms_fonts_certs
                    )

                    val callback: FontsContractCompat.FontRequestCallback =
                        object : FontsContractCompat.FontRequestCallback() {
                            override fun onTypefaceRetrieved(typeface: Typeface) {
                                lato = typeface

                                var finalHeight = 0
                                val myTextPaint =
                                    TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)

                                data.forEach {
                                    var tempHeight = 0
                                    if (it.title.isNotEmpty()) {
                                        myTextPaint.typeface = merriweather
                                        myTextPaint.textSize =
                                            parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_title_size)
                                        myTextPaint.letterSpacing = 0f
                                        val builder =
                                            StaticLayout.Builder.obtain(
                                                it.title,
                                                0,
                                                it.title.length,
                                                myTextPaint,
                                                MeasureUtil.getWidthOfHorizontalBannerItem(
                                                    parent.context,
                                                    parentWidth
                                                )
                                            )
                                                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                                                //.setLineSpacing(parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_letter_spacing), 1.0f)
                                                .setIncludePad(true)
                                        val myStaticLayout = builder.build()
                                        tempHeight = tempHeight + myStaticLayout.height
                                        tempHeight =
                                            tempHeight + FrameworkUtil.getDimenstionInPixels(
                                                parent.context,
                                                R.dimen.banner_list_item_horizontal_item_title_top_margin
                                            )
                                    }
                                    if (it.description.isNotEmpty()) {
                                        myTextPaint.typeface = lato
                                        myTextPaint.textSize =
                                            parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_subtitle_size)
                                        myTextPaint.letterSpacing = ResourcesCompat.getFloat(
                                            parent.resources,
                                            R.dimen.banner_list_item_horizontal_item_letter_spacing
                                        )
                                        val des = it.description


                                        val builder =
                                            StaticLayout.Builder.obtain(
                                                des,
                                                0,
                                                des.length,
                                                myTextPaint,
                                                MeasureUtil.getWidthOfHorizontalBannerItem(
                                                    parent.context,
                                                    parentWidth
                                                )
                                            )
                                                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                                                .setLineSpacing(
                                                    parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_line_spacing_extra),
                                                    1.0f
                                                )
                                                .setIncludePad(true)
                                        val myStaticLayout = builder.build()
                                        tempHeight = tempHeight + myStaticLayout.height
                                        tempHeight =
                                            tempHeight + FrameworkUtil.getDimenstionInPixels(
                                                parent.context,
                                                R.dimen.banner_list_item_horizontal_item_subtitle_top_margin
                                            )
                                    }
                                    if (it.button_title.isNotEmpty()) {
                                        myTextPaint.typeface = lato
                                        myTextPaint.textSize =
                                            parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_action_button_size)
                                        myTextPaint.letterSpacing = ResourcesCompat.getFloat(
                                            parent.resources,
                                            R.dimen.banner_list_item_horizontal_item_letter_spacing
                                        )

                                        val builder =
                                            StaticLayout.Builder.obtain(
                                                it.button_title,
                                                0,
                                                it.button_title.length,
                                                myTextPaint,
                                                MeasureUtil.getWidthOfHorizontalBannerItem(
                                                    parent.context,
                                                    parentWidth
                                                )
                                            )
                                                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                                                .setLineSpacing(
                                                    parent.resources.getDimension(R.dimen.banner_list_item_horizontal_item_line_spacing_extra),
                                                    1.0f
                                                )
                                                .setIncludePad(true)
                                        val myStaticLayout = builder.build()
                                        tempHeight = tempHeight + myStaticLayout.height
                                        tempHeight =
                                            tempHeight + FrameworkUtil.getDimenstionInPixels(
                                                parent.context,
                                                R.dimen.banner_list_item_horizontal_item_action_button_top_margin
                                            )
                                    }
                                    if (tempHeight > finalHeight) {
                                        finalHeight = tempHeight
                                    }

                                }
                                textBlockHeight = finalHeight
                                //parent.requestLayout()
                                notifyDataSetChanged()
                            }
                        }
                    FontsContractCompat.requestFont(
                        parent.context,
                        requestLato,
                        callback,
                        getHandlerThreadHandlerLato()
                    )
                }
            }
        FontsContractCompat.requestFont(
            parent.context,
            request,
            callback,
            getHandlerThreadHandler()
        )
    }

    private fun getHandlerThreadHandler(): Handler {
        if (mHandler == null) {
            val handlerThread = HandlerThread("fonts")
            handlerThread.start()
            mHandler = Handler(handlerThread.looper)
        }
        mHandler?.let {
            return it
        } ?: run {
            throw NullPointerException()
        }

    }

    private fun getHandlerThreadHandlerLato(): Handler {
        if (mHandlerLato == null) {
            val handlerThread = HandlerThread("fontsLato")
            handlerThread.start()
            mHandlerLato = Handler(handlerThread.looper)
        }
        mHandlerLato?.let {
            return it
        } ?: run {
            throw NullPointerException()
        }

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mHandler?.removeCallbacksAndMessages(null)
        mHandler = null
        mHandlerLato?.removeCallbacksAndMessages(null)
        mHandlerLato = null

    }
}