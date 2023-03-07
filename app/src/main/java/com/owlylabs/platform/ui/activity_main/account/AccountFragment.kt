package com.owlylabs.platform.ui.account

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.owlylabs.platform.PlatformApplication
import com.owlylabs.platform.R
import com.owlylabs.platform.model.data.RecommendedData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.account.list.AccountFragmentRecyclerViewAdapter
import com.owlylabs.platform.ui.account.list.AccountFragmentRecyclerViewItemDecorations
import com.owlylabs.platform.util.FrameworkUtil
import com.owlylabs.platform.util.InternetUtil
import com.owlylabs.platform.util.ShareUtil
import com.owlylabs.platform.util.SupportUtil
import com.owlylabs.platform.constants.AppLogicConstants
import dagger.android.support.DaggerFragment
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import kotlin.properties.Delegates


class AccountFragment : DaggerFragment(),
    AccountFragmentRecyclerViewAdapter.ItemListClickListener,
    InterfaceShowEmailDialog {

    private lateinit var viewModel: AccountViewModel
    private lateinit var mContext: Context
    protected var tabId: Int by Delegates.notNull()

    private lateinit var appbarlayout: AppBarLayout
    private lateinit var materialToolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var repository: AbstractLocalRepository

    @Inject
    lateinit var localRepository: AbstractLocalRepository

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { args ->
            tabId = args.getInt(AppLogicConstants.ARG_TAB_ID)
        }
        viewModel = ViewModelProvider(
            this,
            AccountViewModel.Factory(
                repository,
                tabId,
                PlatformApplication.get()
            )
        ).get(tabId.toString(), AccountViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(fragmentView: View, savedInstanceState: Bundle?) {
        findViews(fragmentView)
        configViews(fragmentView)
        compositeDisposable = CompositeDisposable()
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            AppLogicConstants.TAG_IS_EMAIL_DIALOG_SUCCESS)?.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().currentBackStackEntry?.savedStateHandle?.remove<Boolean>(
                    AppLogicConstants.TAG_IS_EMAIL_DIALOG_SUCCESS)
                onContactSupport()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(fragmentView) { v, insets ->
            (materialToolbar.layoutParams as AppBarLayout.LayoutParams).run {
                topMargin = insets.systemWindowInsetTop
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

            recyclerView.setPadding(
                0,
                appbarlayout.measuredHeight,
                0,
                FrameworkUtil.getBottomNavigationViewHeight(mContext) + resources.getDimensionPixelOffset(
                    R.dimen.fragment_book_detail_bottom_text_margin
                )
            )

            insets.consumeSystemWindowInsets()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.tabLiveData.observe(this, Observer { tabData ->
            materialToolbar.title = tabData.title
        })
        viewModel.listLiveData.observe(this, Observer {
            val adapter = recyclerView.adapter
            if (adapter is AccountFragmentRecyclerViewAdapter) {
                adapter.updateData(it)
            }
        })
    }

    override fun onStop() {
        super.onStop()
        viewModel.tabLiveData.removeObservers(this)
        viewModel.listLiveData.removeObservers(this)
    }

    private fun findViews(fragmentView: View) {
        appbarlayout = fragmentView.findViewById(R.id.app_bar_layout)
        materialToolbar = fragmentView.findViewById(R.id.material_toolbar)
        recyclerView = fragmentView.findViewById(R.id.recyclerView)
    }

    private fun configViews(fragmentView: View) {
        configRecyclerView()
    }

    private fun configRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(mContext, 1, RecyclerView.VERTICAL, false)
        recyclerView.addItemDecoration(AccountFragmentRecyclerViewItemDecorations(mContext))
        recyclerView.adapter = AccountFragmentRecyclerViewAdapter(this)
    }

    override fun onChangeName() {
        val action = AccountFragmentDirections.actionAccountFragmentToNameDialog()
        findNavController().navigate(action)
    }

    override fun onClickManageSubscription() {

        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/account/subscriptions")
                )
            );
        } catch (e: ActivityNotFoundException) {

        }

    }

    override fun onContactSupport() {
        SupportUtil.writeToSupport(mContext, localRepository, this)
    }

    override fun onRateThisApp() {
        SupportUtil.rateThisApp(mContext)
    }

    override fun onRecommendThisApp() {
        ShareUtil.recommendOurApp(mContext)
    }

    override fun onBannerClick(banner: RecommendedData) {
        InternetUtil.openLink(mContext, banner.url_android)
    }

    override fun onDeveloperLogoClick() {
        InternetUtil.openLink(mContext, AppLogicConstants.BRAND_URL)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()

    }

    override fun showEmailDialog() {
        val action = AccountFragmentDirections.actionAccountFragmentToEmailDialog()
        findNavController().navigate(action)
    }
}
