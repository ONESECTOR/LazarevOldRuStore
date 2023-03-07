package com.owlylabs.platform.ui.activity_main

import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.owlylabs.platform.billing.BillingClientManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.owlylabs.platform.R
import com.owlylabs.platform.constants.ApiConstants
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.services.ReloadServerDataService
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.constants.AppLogicConstants
import com.owlylabs.platform.databinding.ActivityMainBinding
import com.owlylabs.platform.model.data.TabData
import com.owlylabs.platform.util.ToastUtil
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), INavToFragmentByDeepLink {

    private val compositeDisposableOnCreate = CompositeDisposable()

    @Inject
    lateinit var tabsRepositoryRoom: AbstractLocalRepository

    @Inject
    lateinit var billingClientManager: BillingClientManager

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var navHostFragment: NavHostFragment

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        configWindowBars()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initViews()
        compositeDisposableOnCreate.add(tabsRepositoryRoom.getAllTabsObservable().observeOn(
            AndroidSchedulers.mainThread()
        ).subscribe(
            {
                configTabsFromLoadedData(it)
                NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController);
            },
            { error -> ToastUtil.showError(this, error) }
        ))
        if (intent.getBooleanExtra(AppLogicConstants.TAG_NEED_RELOAD_DATA, false)) {
            ReloadServerDataService.startActionFullDownload(this, false)
        }
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    private fun initViews() {
        bottomNavigationView = findViewById(R.id.nav_view)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposableOnCreate.dispose()
    }

    private fun configTabsFromLoadedData(newMenuData: List<TabData>) {

        if (newMenuData.isNotEmpty()){
            val mainTab = newMenuData[0]
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.main_navigation)

            when (mainTab.tabType) {
                ApiConstants.TAB_TYPE_NEWS ->   graph.startDestination = R.id.newsFragment
                ApiConstants.TAB_TYPE_BOOKS -> graph.startDestination = R.id.booksFragment
                ApiConstants.TAB_TYPE_AUDIOS -> graph.startDestination = R.id.audiosFragment
                ApiConstants.TAB_TYPE_VIDEOS -> graph.startDestination = R.id.videosFragment
                ApiConstants.TAB_TYPE_ACCOUNT -> graph.startDestination = R.id.accountFragment
            }

            newMenuData.forEach { tabData ->
                when (tabData.tabType) {
                    ApiConstants.TAB_TYPE_NEWS -> {
                        graph.findNode(R.id.newsFragment)!!.addArgument("tabId",NavArgument.Builder().setDefaultValue(tabData.itemId).build())
                    }
                    ApiConstants.TAB_TYPE_BOOKS -> {
                        graph.findNode(R.id.booksFragment)!!.addArgument("tabId",NavArgument.Builder().setDefaultValue(tabData.itemId).build())
                    }
                    ApiConstants.TAB_TYPE_AUDIOS -> {
                        graph.findNode(R.id.audiosFragment)!!.addArgument("tabId",NavArgument.Builder().setDefaultValue(tabData.itemId).build())
                    }
                    ApiConstants.TAB_TYPE_VIDEOS -> {
                        graph.findNode(R.id.videosFragment)!!.addArgument("tabId",NavArgument.Builder().setDefaultValue(tabData.itemId).build())
                    }
                    ApiConstants.TAB_TYPE_ACCOUNT -> {
                        graph.findNode(R.id.accountFragment)!!.addArgument("tabId",NavArgument.Builder().setDefaultValue(tabData.itemId).build())
                    }
                }
            }

            val args = Bundle()
            args.putInt("tabId", mainTab.itemId)

            navHostFragment.navController.graph = graph
        }
    }

    private fun configWindowBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or
                            View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = getColor(android.R.color.white)
                window.navigationBarDividerColor = getColor(R.color.navigation_bar_divider)
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = getColor(android.R.color.white)
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                window.navigationBarColor = getColor(android.R.color.white)
            } else {
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    override fun navigateToFragmentByDeepLink(uri: Uri) {
        val builder = NavOptions.Builder()
        val navOptions = builder
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()
        Navigation.findNavController(findViewById<FragmentContainerView>(R.id.nav_host_fragment))
            .navigate(uri, navOptions)
    }
}
