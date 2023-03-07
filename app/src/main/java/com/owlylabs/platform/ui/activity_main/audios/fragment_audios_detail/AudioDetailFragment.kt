package com.owlylabs.platform.ui.audios.fragment_audios_detail

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
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
import com.owlylabs.platform.services.AudioService
import com.owlylabs.platform.ui.custom.NestedScrollViewWithManualSaveState
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.util.ShareUtil
import com.owlylabs.platform.util.ViewUtil
import com.owlylabs.platform.util.WebViewUtil
import com.owlylabs.playerlibrary.OwlyPlayer
import com.owlylabs.playerlibrary.model.data.PlaylistData
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import jp.wasabeef.glide.transformations.BlurTransformation
import javax.inject.Inject
import kotlin.properties.Delegates


class AudioDetailFragment : DaggerFragment() {

    private var audioBookId: Int by Delegates.notNull()

    private lateinit var webView: WebView
    private lateinit var imageViewBackground: ImageView
    private lateinit var imageViewBook: ImageView
    private lateinit var textViewBookTitle: TextView
    private lateinit var materialToolbar: MaterialToolbar
    //private lateinit var kk = PlayerService
    private lateinit var nestedScrollView: NestedScrollViewWithManualSaveState
    private lateinit var appbarlayout: AppBarLayout
    private lateinit var buttonPlay: MaterialButton
    private lateinit var linearLayoutContainer: LinearLayout

    private lateinit var webViewString: String
    private var nestedScrollViewState: Parcelable? = null
    private lateinit var mContext: Context



    @Inject
    lateinit var repository: AbstractLocalRepository

    private lateinit var viewModel: AudioDetailViewModel
    private lateinit var bookServiceConnection: ServiceConnection
    private var bookServiceBinder: AudioService.AudioServiceBinder? = null

    private var serviceDisposable: Disposable? = null

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
            audioBookId = it.getInt("audioId")
        }
        viewModel =
            ViewModelProvider(this, AudioDetailViewModel.Factory(repository, audioBookId)).get(
                AudioDetailViewModel::class.java
            )
        bookServiceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
                if (binder is AudioService.AudioServiceBinder) {
                    bookServiceBinder = binder
                    serviceDisposable =
                        binder.getSubject()
                            .filter {
                                it.first.idBook == audioBookId.toString()
                            }.subscribeBy(
                                onNext = {
                                    OwlyPlayer.play(
                                        mContext,
                                        PlaylistData(
                                            audioBookId,
                                            ApiConstants.API_PARAM_CONST_APP_ID,
                                            it.second
                                        ).playlistName(it.first.nameBook).playlistAuthor(getString(R.string.app_name))
                                    )
                                }
                            )
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                //serviceDisposable?.dispose()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_audio_detail, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(fragmentView, savedInstanceState)
        webView = fragmentView.findViewById(R.id.bookWebView)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = false
        webView.settings.setNeedInitialFocus(false)
        imageViewBackground = fragmentView.findViewById(R.id.image_background)
        imageViewBook = fragmentView.findViewById(R.id.image_book)
        textViewBookTitle = fragmentView.findViewById(R.id.book_title)
        materialToolbar = fragmentView.findViewById(R.id.toolbar)
        appbarlayout = fragmentView.findViewById(R.id.appbarlayout)
        appbarlayout.isSelected = true
        //appbarlayout.outlineProvider = null
        appbarlayout.stateListAnimator = ViewUtil.getAppBarLayoutStateListAnimatorForDetailFragments(mContext, appbarlayout)
        buttonPlay = fragmentView.findViewById(R.id.button_listen)
        linearLayoutContainer = fragmentView.findViewById(R.id.linearLayoutContainer)
        //buttonPlay.bringToFront()
        carview_container = fragmentView.findViewById(R.id.carview_container)
        materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_share_with){
                disposableGetBookName = repository.
                    getAudioNameByIdBlocking(audioBookId).
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

        val nestedScrollViewLP = nestedScrollView.layoutParams as CoordinatorLayout.LayoutParams
        //nestedScrollViewLP.bottomMargin = FrameworkUtil.getBottomNavigationViewHeight(mContext) + resources.getDimensionPixelOffset(R.dimen.fragment_audio_detail_bottom_text_margin)

        cardView = fragmentView.findViewById(R.id.cardView)

        val cardview_container_lp = carview_container.layoutParams as ConstraintLayout.LayoutParams
        cardview_container_lp.topMargin = FrameworkUtil.getToolbarHeight(mContext) + FrameworkUtil.getStatusBarHeight(mContext)

        nestedScrollView.setPadding(0,
            0,
            0,
            FrameworkUtil.getBottomNavigationViewHeight(mContext) + resources.getDimensionPixelOffset(R.dimen.fragment_audio_detail_bottom_text_margin))

        ViewCompat.setOnApplyWindowInsetsListener(fragmentView) { v, insets ->
            //appbarlayout.updatePadding(insets.systemWindowInsetLeft, insets.systemWindowInsetTop, insets.systemWindowInsetRight, 0)

            (materialToolbar.layoutParams as AppBarLayout.LayoutParams).run {
                topMargin = insets.systemWindowInsetTop
                //leftMargin = insets.systemWindowInsetLeft
                //rightMargin = insets.systemWindowInsetRight
            }
            materialToolbar.updatePadding(
                insets.systemWindowInsetLeft,
                0,
                insets.systemWindowInsetRight,
                0
            )

            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(
                View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.AT_MOST
            )
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
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
        mContext.bindService(Intent(mContext, AudioService::class.java), bookServiceConnection, 0)
        viewModel.bookLiveData.observe(this, Observer {
            val newTextForWebView = WebViewUtil.overrideTextToRemovePaddings(it.description)
            webView.evaluateJavascript("(function(){return window.document.body.outerHTML})();",
                object : ValueCallback<String> {
                    override fun onReceiveValue(html: String) {
                        if (html != newTextForWebView) {
                            reloadWebViewText(newTextForWebView)
                        }
                    }
                });

            textViewBookTitle.text = it.nameBook

            val multi = MultiTransformation<Bitmap>(
                BlurTransformation(75),
                RoundedCorners(mContext.resources.getDimensionPixelSize(R.dimen.video_detail_image_radius))
            )

            Glide.with(mContext)
                .load(ApiConstants.API_BASE_URL.plus(it.urlLargeImg))
                .apply(RequestOptions.bitmapTransform(multi))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewBackground)

            Glide.with(mContext)
                .load(ApiConstants.API_BASE_URL.plus(it.urlLargeImg))
                .apply(
                    RequestOptions().transform(
                        RoundedCorners(
                            mContext.resources.getDimensionPixelSize(
                                R.dimen.video_detail_image_radius
                            )
                        )
                    )
                )
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewBook)

            buttonPlay.setOnClickListener(View.OnClickListener { v ->
                AudioService.openBook(mContext, audioBookId, ApiConstants.API_PARAM_CONST_APP_ID)
            })
        })
    }

    override fun onStop() {
        super.onStop()
        mContext.unbindService(bookServiceConnection)
        viewModel.bookLiveData.removeObservers(this)
        serviceDisposable?.dispose()
    }

    private fun reloadWebViewText(text: String) {
        webViewString = text
        webView.loadDataWithBaseURL(
            null,

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

    override fun onDestroyView() {
        disposableGetBookName?.dispose()
        nestedScrollViewState = nestedScrollView.onSaveInstanceState()
        super.onDestroyView()
    }
}
