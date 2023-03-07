package com.owlylabs.platform.ui.books.fragment_books_collection

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.owlylabs.platform.R
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.constants.AppLogicConstants
import dagger.android.support.DaggerFragment
import javax.inject.Inject
import kotlin.properties.Delegates


class BooksCollectionFragment : DaggerFragment() {
    private var sectionId: Int by Delegates.notNull()

    @Inject
    lateinit var repository: AbstractLocalRepository

    private lateinit var mContext: Context
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var viewModel: BooksCollectionViewModel

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var collapsing_toolbar: CollapsingToolbarLayout
    private lateinit var recyclerView: RecyclerView
    protected lateinit var recyclerViewAdapter: BooksCollectionRecyclerViewAdapter

    private var gridLayoutManagerState: Parcelable? = null
    private lateinit var INavToFragmentByDeepLink: INavToFragmentByDeepLink

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        if (context is INavToFragmentByDeepLink){
            INavToFragmentByDeepLink = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionId = it.getInt("sectionId")
        }
        viewModel = ViewModelProvider(
            this,
            BooksCollectionViewModel.Factory(
                repository,
                sectionId
            )
        ).get(sectionId.toString(), BooksCollectionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_books_collection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        handleInsets(view)
        configRecyclerView()
    }

    override fun onStart() {
        super.onStart()

    }

    private fun setLayoutForCollapsingToolbar(tabData: SectionData){
        tabData.title?.let { text ->
            val listener = ViewTreeObserver.OnPreDrawListener {
                false
            }

            val textToShow = tabData.title

            collapsing_toolbar.viewTreeObserver.addOnPreDrawListener(listener)

            collapsing_toolbar.title = textToShow

            val myTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG or Paint.SUBPIXEL_TEXT_FLAG)
            myTextPaint.textSize = 19 * resources.displayMetrics.density
            myTextPaint.typeface = Typeface.SANS_SERIF

            val metrics: DisplayMetrics = DisplayMetrics();
            requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
            val sreenWidth = metrics.widthPixels - FrameworkUtil.convertDpToPixels(24f, mContext).toInt()
            val builder =
                StaticLayout.Builder.obtain(
                        textToShow,
                        0,
                        textToShow.length,
                        myTextPaint,
                        sreenWidth
                    )
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(ResourcesCompat.getFloat(resources, R.dimen.banner_list_item_horizontal_item_letter_spacing), 1.0f)
                    .setIncludePad(true)
                    .setMaxLines(4)

            val myStaticLayout = builder.build()
            val heightOfText = myStaticLayout.height
            var finalToolbarHeight = 0


            val rootWindowInsets = requireActivity().window.decorView.rootWindowInsets

            var heightOfStatusBar = 0

            rootWindowInsets?.let {
                heightOfStatusBar = it.systemWindowInsetTop
            } ?: run{
                heightOfStatusBar = FrameworkUtil.getStatusBarHeight(mContext)
            }
            val heightOfToolbar = FrameworkUtil.getToolbarHeight(mContext)

            val testHeight =
                if (heightOfStatusBar < heightOfText) heightOfText else heightOfStatusBar

            finalToolbarHeight = testHeight + heightOfToolbar

            val lp = collapsing_toolbar.layoutParams as AppBarLayout.LayoutParams
            System.out.println("lll".plus(finalToolbarHeight))
            lp.height = finalToolbarHeight

            collapsing_toolbar.viewTreeObserver.removeOnPreDrawListener(listener)
            collapsing_toolbar.invalidate()
            collapsing_toolbar.requestLayout();
        }
    }

    override fun onStop() {
        super.onStop()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.sectionData.observe(viewLifecycleOwner, Observer { tabData ->
            System.out.println("lll2")
            setLayoutForCollapsingToolbar(tabData)


            tabData.backgroundColor?.let {
                val color = Color.parseColor(it)
                appBarLayout.setBackgroundColor(color)
                collapsing_toolbar.setContentScrimColor(color)
            }
        })
        viewModel.listLiveData.observe(viewLifecycleOwner, Observer {
            recyclerViewAdapter.updateData(it)
        })
    }

    override fun onDestroyView() {
        gridLayoutManagerState = gridLayoutManager.onSaveInstanceState()
        super.onDestroyView()

    }

    private fun initViews(parent: View) {
        appBarLayout = parent.findViewById(R.id.app_bar_layout)
        materialToolbar = parent.findViewById(R.id.material_toolbar)
        recyclerView = parent.findViewById(R.id.recycler_view)
        collapsing_toolbar = parent.findViewById(R.id.collapsing_toolbar)

        materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


    }

    private fun handleInsets(fragmentView: View) {
        ViewCompat.setOnApplyWindowInsetsListener(fragmentView) { v, insets ->
            /* 1. Set padding for AppBarLayout + custom shadow */
            System.out.println("lll0")
            var needReloadLayout = true

            val listener = ViewTreeObserver.OnPreDrawListener {
                false
            }

            appBarLayout.viewTreeObserver.addOnPreDrawListener(listener)
            collapsing_toolbar.viewTreeObserver.addOnPreDrawListener(listener)
            appBarLayout.updatePadding(
                insets.systemWindowInsetLeft,
                insets.systemWindowInsetTop,
                insets.systemWindowInsetRight,
                0
            )

            var sectionData: SectionData? = null

            viewModel.sectionData.value?.let {
                System.out.println("lll1")
                sectionData = it
                needReloadLayout = false
            } ?: run {
                needReloadLayout = true
            }

            appBarLayout.viewTreeObserver.removeOnPreDrawListener(listener)
            collapsing_toolbar.viewTreeObserver.removeOnPreDrawListener(listener)

            if (!needReloadLayout) {
                sectionData?.let {
                    setLayoutForCollapsingToolbar(it)
                }
            } else {
                appBarLayout.invalidate()
                appBarLayout.requestLayout()
            }

            recyclerView.updatePadding(
                recyclerView.paddingStart, 0,
                recyclerView.paddingEnd, FrameworkUtil.getBottomNavigationViewHeight(mContext)
            )

            /* 5. ConsumeSystemWindowInsets to disable default system insets padding behaviour */
            insets.consumeSystemWindowInsets()
        }
    }

    private fun configRecyclerView() {
        recyclerViewAdapter =
            BooksCollectionRecyclerViewAdapter(
                INavToFragmentByDeepLink
            )
        gridLayoutManager =
            GridLayoutManager(mContext, resources.getInteger(R.integer.numberOfBookCollectionGrid))
        gridLayoutManager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (recyclerViewAdapter.getItemViewType(position)) {
                    AppLogicConstants.BookCollectionViewHolderType.BOOK.ordinal -> resources.getInteger(R.integer.numberOfBookCollectionBookHolder)
                    AppLogicConstants.BookCollectionViewHolderType.SUBTITLE.ordinal -> resources.getInteger(R.integer.numberOfBookCollectionDescription)

                    else -> -1
                }
            }
        })
        gridLayoutManagerState?.let {
            gridLayoutManager.onRestoreInstanceState(gridLayoutManagerState)
        }
        recyclerView.addItemDecoration(
            BooksCollectionRecyclerViewItemDecoration(
                mContext
            )
        )
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = recyclerViewAdapter
    }
}
