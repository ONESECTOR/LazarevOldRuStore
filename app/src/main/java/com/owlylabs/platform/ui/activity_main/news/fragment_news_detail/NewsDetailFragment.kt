package com.owlylabs.platform.ui.news.fragment_news_detail

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.databinding.FragmentNewsDetailBinding
import com.owlylabs.platform.model.data.JumpData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.ui.custom.NestedScrollViewWithManualSaveState
import com.owlylabs.platform.util.DeepLinkUtil
import com.owlylabs.platform.util.InternetUtil
import com.owlylabs.platform.util.ViewUtil
import com.owlylabs.platform.util.WebViewUtil
import dagger.android.support.DaggerFragment
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates


class NewsDetailFragment : DaggerFragment(),
    OnBottomNavigationViewBecameHiddenListener {
    // TODO: Rename and change types of parameters
    private var argNewsId: Int by Delegates.notNull()
    private lateinit var newsDetailBinding: FragmentNewsDetailBinding
    private lateinit var newsDetailViewModel: NewsDetailViewModel

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: AbstractLocalRepository

    private lateinit var webView: WebView
    private lateinit var newsImg: ImageView
    private lateinit var appbarlayout: AppBarLayout
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var nestedScrollView: NestedScrollViewWithManualSaveState
    private lateinit var collapsing_toolbar: CollapsingToolbarLayout

    private var isNeedShowJumpBottomSheet = false
    private var hasJumpBottomSheetEverShowed = false
    private var wasAnyAttemptToShowJumpBottomSheet = false
    private lateinit var rootView: ViewGroup
    private lateinit var webViewString: String
    private var nestedScrollViewState: Parcelable? = null

    private lateinit var INavToFragmentByDeepLink: INavToFragmentByDeepLink

    private lateinit var mContext: Context
    private var argDoNotWaitMenuAnimation: Int by Delegates.notNull()

    private var appBarLayoutOffset = 0

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
            argNewsId = it.getInt("newsId")
            argDoNotWaitMenuAnimation = it.getInt("doNotWaitMenuAnimation")
        }
        newsDetailViewModel =
            ViewModelProvider(this, NewsDetailViewModel.Factory(repository, argNewsId, argDoNotWaitMenuAnimation)).get(
                NewsDetailViewModel::class.java
            )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        newsDetailBinding = FragmentNewsDetailBinding.inflate(inflater, container, false).apply {
            viewModel = newsDetailViewModel
            lifecycleOwner = this@NewsDetailFragment
        }
        rootView = newsDetailBinding.root as ViewGroup
        return newsDetailBinding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.newsWebView)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = false
        webView.settings.setNeedInitialFocus(false)
        newsImg = view.findViewById(R.id.newsImg)
        materialToolbar = view.findViewById(R.id.toolbar)
        appbarlayout = view.findViewById(R.id.appbarlayout)
        collapsing_toolbar = view.findViewById(R.id.collapsing_toolbar)
        materialToolbar.setNavigationOnClickListener {
            ViewUtil.clearLightStatusBar(requireActivity())
            findNavController().popBackStack()
        }
        nestedScrollView = view.findViewById(R.id.nestedScrollView)
        appbarlayout.addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            appBarLayoutOffset = verticalOffset
            dispatchCollapsingToolbarScrimState()
        })

        ViewCompat.setOnApplyWindowInsetsListener(appbarlayout) { v, insets ->
            (materialToolbar.layoutParams as FrameLayout.LayoutParams).run {
                topMargin = insets.systemWindowInsetTop
                //leftMargin = insets.systemWindowInsetLeft
                //rightMargin = insets.systemWindowInsetRight
            }
            materialToolbar.updatePadding(insets.systemWindowInsetLeft, 0, insets.systemWindowInsetRight, 0)
            insets.consumeSystemWindowInsets()
        }
    }

    private fun dispatchCollapsingToolbarScrimState(){
        val scrimValue = collapsing_toolbar.scrimVisibleHeightTrigger
        val isScrimed = (appbarlayout.height + appBarLayoutOffset) <= scrimValue
        if(isScrimed){
            ViewUtil.setLightStatusBar(requireActivity())
            materialToolbar.navigationIcon =
                ContextCompat.getDrawable(mContext, R.drawable.vector_close_black)
        } else {
            ViewUtil.clearLightStatusBar(requireActivity())
            materialToolbar.navigationIcon =
                ContextCompat.getDrawable(mContext, R.drawable.vector_close_white)
        }
    }

    override fun onStart() {
        super.onStart()

        dispatchCollapsingToolbarScrimState()

        newsDetailViewModel.newsLiveData.observe(this, Observer {
            val newTextForWebView = WebViewUtil.overrideTextToRemovePaddings(it.text)
            webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                object : ValueCallback<String> {
                    override fun onReceiveValue(html: String) {
                        if (html != newTextForWebView){
                            reloadWebViewText(newTextForWebView)
                        }
                    }
                });


            Glide.with(mContext)
                .load(ApiConstants.API_BASE_URL.plus(it.url_large_img))
                .into(newsImg)
        })
        newsDetailViewModel.jumpLiveData.observe(this, Observer {
            showJumpBottomSheet(it)
        })
    }

    private fun reloadWebViewText(text: String){
        webViewString = text
        webView.loadDataWithBaseURL(null,

            webViewString,
            "text/html",
            "UTF-8",
            null
        )
        nestedScrollViewState?.let {
            nestedScrollView.onRestoreInstanceState(it)
            nestedScrollViewState = null
        }
    }

    override fun onStop() {
        super.onStop()
        newsDetailViewModel.newsLiveData.removeObservers(this)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        nestedScrollViewState = nestedScrollView.onSaveInstanceState()
        super.onDestroyView()
    }

    override fun onBottomNavigationViewBecameHidden() {
        newsDetailViewModel.isTheBottomNavigationViewClosed.value = true
    }

    fun showJumpBottomSheet(jumpInfo: JumpData) {
        when (jumpInfo.jumpType) {
            1, 2, 3, 4, 5 -> {
                val bottomSheetContainer =
                    newsDetailBinding.root.findViewById<FrameLayout>(R.id.fragment_news_details_bottom_sheet_parent)
                if (bottomSheetContainer == null) {
                    val lp = CoordinatorLayout.LayoutParams(
                        CoordinatorLayout.LayoutParams.MATCH_PARENT,
                        CoordinatorLayout.LayoutParams.WRAP_CONTENT
                    )
                    lp.behavior = BottomSheetBehavior<FrameLayout>()
                    val bottomSheet = layoutInflater.inflate(
                        R.layout.fragment_news_detail_bottom_sheet,
                        rootView,
                        false
                    )
                    bottomSheet.layoutParams = lp
                    val textView = bottomSheet.findViewById<TextView>(R.id.jump_text)
                    when (jumpInfo.jumpType) {
                        1 -> {
                            textView.text = getString(R.string.jump_to_web)
                            bottomSheet.setOnClickListener {
                                InternetUtil.openLink(mContext, jumpInfo.jumpData)
                            }
                            textView.setCompoundDrawablesWithIntrinsicBounds(
                                0,
                                0,
                                R.drawable.vector_open_link,
                                0
                            )
                        }
                        2 -> {
                            textView.text = getString(R.string.jump_to_news)
                            bottomSheet.setOnClickListener {
                                val uri = DeepLinkUtil.createNewsDetailDeepLink(jumpInfo.jumpData.toInt(), 1)
                                INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
                            }
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                        3 -> {
                            textView.text = getString(R.string.jump_to_books)
                            bottomSheet.setOnClickListener {
                                val uri = DeepLinkUtil.createBookDetailDeepLink(jumpInfo.jumpData.toInt())
                                INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
                            }
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                        4 -> {
                            textView.text = getString(R.string.jump_to_audio)
                            bottomSheet.setOnClickListener {
                                val uri =
                                    DeepLinkUtil.createAudioDetailDeepLink(jumpInfo.jumpData.toInt())
                                INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
                            }
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        }
                        5 -> {
                            textView.text = getString(R.string.jump_to_video)
                            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            bottomSheet.setOnClickListener {
                                val uri = DeepLinkUtil.createVideoDetailDeepLink(jumpInfo.jumpData.toInt())
                                INavToFragmentByDeepLink.navigateToFragmentByDeepLink(uri)
                            }
                        }
                    }
                    rootView.addView(bottomSheet)
                    Completable.timer(
                        resources.getInteger(android.R.integer.config_mediumAnimTime).toLong(),
                        TimeUnit.MILLISECONDS,
                        AndroidSchedulers.mainThread()
                    ).subscribe {
                        BottomSheetBehavior.from(bottomSheet as View).state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                } else {
                    Completable.timer(
                        resources.getInteger(android.R.integer.config_mediumAnimTime).toLong(),
                        TimeUnit.MILLISECONDS,
                        AndroidSchedulers.mainThread()
                    ).subscribe {
                        BottomSheetBehavior.from(bottomSheetContainer).state =
                            BottomSheetBehavior.STATE_EXPANDED
                    }
                }
            }
        }
    }




}