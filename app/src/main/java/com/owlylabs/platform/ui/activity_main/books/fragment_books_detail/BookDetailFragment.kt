package com.owlylabs.platform.ui.books.fragment_books_detail

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.owlylabs.epublibrary.BookRequest
import com.owlylabs.epublibrary.OwlyReader
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.services.BookService
import com.owlylabs.platform.ui.custom.NestedScrollViewWithManualSaveState
import com.owlylabs.platform.util.*
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import jp.wasabeef.glide.transformations.BlurTransformation
import javax.inject.Inject
import kotlin.properties.Delegates


class BookDetailFragment : DaggerFragment() {
    private var bookId: Int by Delegates.notNull()

    @Inject
    lateinit var repository: AbstractLocalRepository

    private lateinit var webView: WebView
    private lateinit var imageViewBackground: ImageView
    private lateinit var imageViewBook: ImageView
    private lateinit var textViewBookTitle: TextView
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var nestedScrollView: NestedScrollViewWithManualSaveState
    private lateinit var appbarlayout: AppBarLayout
    private lateinit var buttonPlay: MaterialButton
    private lateinit var progressBar: ProgressBar
    private lateinit var textviewPageCount: TextView
    private lateinit var textviewBookProgress: TextView

    private lateinit var webViewString: String
    private var nestedScrollViewState: Parcelable? = null

    private lateinit var viewModel: BookDetailViewModel
    private lateinit var mContext: Context

    private lateinit var carview_container: FrameLayout

    private lateinit var cardView: MaterialCardView

    private lateinit var bookServiceConnection: ServiceConnection
    private var bookServiceBinder: BookService.BookServiceBinder? = null

    private var serviceDisposable: Disposable? = null
    private var disposableGetBookName : Disposable? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bookId = it.getInt("bookId")
        }
        viewModel =
            ViewModelProvider(this, BookDetailViewModel.Factory(repository, bookId)).get(
                BookDetailViewModel::class.java
            )
        bookServiceConnection = object : ServiceConnection {

            override fun onServiceConnected(name: ComponentName?, binder: IBinder) {
                if (binder is BookService.BookServiceBinder) {
                    bookServiceBinder = binder
                    serviceDisposable =
                        binder.getSubject().observeOn(AndroidSchedulers.mainThread())
                            .filter {
                                it.idBook == bookId.toString()
                            }
                            .subscribeBy(
                                onNext = {
                                    OwlyReader.read(
                                        BookRequest.Builder(
                                            it.idBook.toInt(),
                                            //Uri.fromFile(File("//assets/d3957710tafti_said.epub"))
                                            Uri.parse(
                                                FileUtil.getBookFilePath(
                                                    mContext,
                                                    it.idBook.toInt()
                                                )
                                            )
                                        )
                                            .setIsFree(true)
                                            .build(), mContext
                                    )
                                }
                            )
                    binder.getListOfCurrentlyDownloadingBookIds().observe(this@BookDetailFragment, Observer {
                        if (it.contains(bookId)){
                            //buttonPlay.setCompoundDrawables(null, null,null, null)
                            //*.iml
                            buttonPlay.isClickable = false
                            buttonPlay.showProgress {
                                buttonText= null
                                progressColor = Color.WHITE
                            }

                        } else {
                            buttonPlay.isClickable = true
                            buttonPlay.hideProgress(R.string.read)
                           // buttonPlay.setCompoundDrawablesWithIntrinsicBounds(R.drawable.vector_read, 0 ,0 , 0)
                        }
                    })
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book_detail, container, false)
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
        progressBar = fragmentView.findViewById(R.id.progress_bar)
        textviewPageCount = fragmentView.findViewById(R.id.textview_page_count)
        textviewBookProgress = fragmentView.findViewById(R.id.textview_book_progress)
        appbarlayout.isSelected = true
        //appbarlayout.outlineProvider = null
        appbarlayout.stateListAnimator = ViewUtil.getAppBarLayoutStateListAnimatorForDetailFragments(mContext, appbarlayout)
        buttonPlay = fragmentView.findViewById(R.id.button_read)
        bindProgressButton(buttonPlay)
        //buttonPlay.attachTextChangeAnimator()
        //buttonPlay.bringToFront()
        carview_container = fragmentView.findViewById(R.id.carview_container)
        materialToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        materialToolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menu_item_share_with){
                disposableGetBookName = repository.
                    getBookNameByIdBlocking(bookId).
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
            FrameworkUtil.getBottomNavigationViewHeight(mContext) + resources.getDimensionPixelOffset(R.dimen.fragment_book_detail_bottom_text_margin))
        cardView = fragmentView.findViewById(R.id.cardView)

        val cardview_container_lp = carview_container.layoutParams as ConstraintLayout.LayoutParams
        cardview_container_lp.topMargin = FrameworkUtil.getToolbarHeight(mContext) + FrameworkUtil.getStatusBarHeight(mContext)

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
        mContext.bindService(Intent(mContext, BookService::class.java), bookServiceConnection, 0)
        viewModel.bookProgressLiveData.observe(this, Observer {
            progressBar.progress = it
            textviewBookProgress.text = getString(R.string.placeholder_read_progress, it)
        })
        viewModel.bookLiveData.observe(this, Observer {
            val newTextForWebView = WebViewUtil.overrideTextToRemovePaddings(it.description)
            textviewPageCount.text = getString(R.string.placeholder_number_of_pages, it.countPage)
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
                .load(ApiConstants.API_BASE_URL.plus(it.urlImg))
                .apply(RequestOptions.bitmapTransform(multi))
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageViewBackground)

            Glide.with(mContext)
                .load(ApiConstants.API_BASE_URL.plus(it.urlImg))
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
                BookService.openBook(mContext, bookId, ApiConstants.API_PARAM_CONST_APP_ID)
            })
        })
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

    override fun onStop() {
        super.onStop()
        mContext.unbindService(bookServiceConnection)
        bookServiceBinder?.getListOfCurrentlyDownloadingBookIds()?.removeObservers(this@BookDetailFragment)
        viewModel.bookLiveData.removeObservers(this)
        viewModel.bookProgressLiveData.removeObservers(this)
        serviceDisposable?.dispose()
    }

    override fun onDestroyView() {
        nestedScrollViewState = nestedScrollView.onSaveInstanceState()
        disposableGetBookName?.dispose()
        super.onDestroyView()
    }

}
