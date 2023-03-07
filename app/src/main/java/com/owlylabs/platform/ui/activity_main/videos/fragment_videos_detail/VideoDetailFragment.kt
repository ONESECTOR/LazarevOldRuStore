package com.owlylabs.platform.ui.videos.fragment_videos_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.ActivityYouTube
import com.owlylabs.platform.ui.custom.NestedScrollViewWithManualSaveState
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.util.ShareUtil
import com.owlylabs.platform.util.ViewUtil
import com.owlylabs.platform.util.WebViewUtil
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import jp.wasabeef.glide.transformations.BlurTransformation
import javax.inject.Inject
import kotlin.properties.Delegates

class VideoDetailFragment : DaggerFragment() {

    private var videoId: Int by Delegates.notNull()

    @Inject
    lateinit var repository: AbstractLocalRepository

    private lateinit var webView: WebView
    private lateinit var imageViewBackground: ImageView
    private lateinit var imageViewVideo: ImageView
    private lateinit var textViewVideoTitle: TextView
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var nestedScrollView: NestedScrollViewWithManualSaveState
    private lateinit var appbarlayout: AppBarLayout
    private lateinit var buttonPlay: MaterialButton

    private lateinit var webViewString: String
    private var nestedScrollViewState: Parcelable? = null

    private lateinit var viewModel: VideoDetailViewModel
    private lateinit var mContext: Context

    private lateinit var carview_container: FrameLayout

    private lateinit var cardView: MaterialCardView

    private var disposableGetBookName : Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            videoId = it.getInt("videoId")
        }
        viewModel =
            ViewModelProvider(this, VideoDetailViewModel.Factory(repository, videoId)).get(
                VideoDetailViewModel::class.java
            )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_detail, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(fragmentView, savedInstanceState)
        webView = fragmentView.findViewById(R.id.videoWebView)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = false
        webView.settings.setNeedInitialFocus(false)
        imageViewBackground = fragmentView.findViewById(R.id.image_background)
        imageViewVideo  = fragmentView.findViewById(R.id.image_video)
        textViewVideoTitle = fragmentView.findViewById(R.id.video_title)
        materialToolbar = fragmentView.findViewById(R.id.toolbar)
        appbarlayout = fragmentView.findViewById(R.id.appbarlayout)
        appbarlayout.isSelected = true
        //appbarlayout.outlineProvider = null
        appbarlayout.stateListAnimator = ViewUtil.getAppBarLayoutStateListAnimatorForDetailFragments(mContext, appbarlayout)
        buttonPlay = fragmentView.findViewById(R.id.button_play)
        //buttonPlay.bringToFront()
        carview_container = fragmentView.findViewById(R.id.carview_container)
        materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_share_with){
                disposableGetBookName = repository.
                    getVideoNameByVideoIdBlocking(videoId).
                    observeOn(AndroidSchedulers.mainThread()).
                    subscribeBy(
                        onError = {},
                        onSuccess = {
                            ShareUtil.shareWithDescriptionContent(mContext, it)
                        })
                return@setOnMenuItemClickListener true
            }
            return@setOnMenuItemClickListener false
        }
        nestedScrollView = fragmentView.findViewById(R.id.nestedScrollView)
        nestedScrollView.setOnScrollChangeListener { v: View?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            appbarlayout.isSelected = scrollY == 0
        }
        nestedScrollView.setPadding(0,
            0,
            0,
            FrameworkUtil.getBottomNavigationViewHeight(mContext) + resources.getDimensionPixelOffset(R.dimen.fragment_video_detail_bottom_text_margin))
        cardView = fragmentView.findViewById(R.id.cardView)

        val cardview_container_lp = carview_container.layoutParams as ConstraintLayout.LayoutParams
        cardview_container_lp.topMargin = FrameworkUtil.getToolbarHeight(mContext) + FrameworkUtil.getStatusBarHeight(mContext)

        ViewCompat.setOnApplyWindowInsetsListener(fragmentView) { v, insets ->

            System.out.println("текст инсеты".plus(System.currentTimeMillis()))
            //appbarlayout.updatePadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, 0)

            (materialToolbar.layoutParams as AppBarLayout.LayoutParams).run {
                topMargin = insets.systemWindowInsetTop
                //leftMargin = insets.systemWindowInsetLeft
                //rightMargin = insets.systemWindowInsetRight
            }
            materialToolbar.updatePadding(insets.systemWindowInsetLeft, 0, insets.systemWindowInsetRight, 0)

            var widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.AT_MOST
            )
            var heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED
            )
            appbarlayout.measure(widthMeasureSpec, heightMeasureSpec)

            val lp = carview_container.layoutParams as ConstraintLayout.LayoutParams
            lp.topMargin = appbarlayout.measuredHeight

            insets.consumeSystemWindowInsets()
        }

        //var windowBackground: Drawable = requireActivity().window.getDecorView().getBackground();
    }

    override fun onStart() {
        super.onStart()
        System.out.println("текст onStart".plus(System.currentTimeMillis()))
        viewModel.videoLiveData.observe(this, Observer {
            val newTextForWebView = WebViewUtil.overrideTextToRemovePaddings(it.description)
            webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                object : ValueCallback<String> {
                    override fun onReceiveValue(html: String) {
                        if (html != newTextForWebView){
                            reloadWebViewText(newTextForWebView)
                        }
                    }
                });

            textViewVideoTitle.text = it.name_book

            val multi = MultiTransformation<Bitmap>(
                BlurTransformation(75),
                RoundedCorners(mContext.resources.getDimensionPixelSize(R.dimen.video_detail_image_radius))
            )

            Glide.with(mContext)
                .load(ApiConstants.API_BASE_URL.plus(it.url_large_img))
                .apply(RequestOptions.bitmapTransform(multi))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewBackground)

            Glide.with(mContext)
                .load(ApiConstants.API_BASE_URL.plus(it.url_large_img))
                .apply(RequestOptions().transform(RoundedCorners(mContext.resources.getDimensionPixelSize(R.dimen.video_detail_image_radius))))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewVideo)

            buttonPlay.setOnClickListener(View.OnClickListener {v ->
                val intent =
                    Intent(mContext, ActivityYouTube::class.java)
                intent.putExtra(
                    "url",
                    it.url_video
                )
                startActivity(intent)
            })
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
        viewModel.videoLiveData.removeObservers(this)
    }

    override fun onDestroyView() {
        disposableGetBookName?.dispose()
        nestedScrollViewState = nestedScrollView.onSaveInstanceState()
        super.onDestroyView()
    }

}
