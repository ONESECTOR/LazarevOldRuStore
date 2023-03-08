package com.owlylabs.platform.ui.activity_main

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.owlylabs.platform.R
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.services.ReloadServerDataService
import com.owlylabs.platform.ui.tab_with_sections.INavToFragmentByDeepLink
import com.owlylabs.platform.constants.AppLogicConstants
import com.owlylabs.platform.databinding.ActivityMainBinding
import com.owlylabs.platform.util.configVisibility
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), INavToFragmentByDeepLink {

    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: AbstractLocalRepository

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var navHostFragment: NavHostFragment

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        configWindowBars()
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            RuStoreBillingClient.onNewIntent(intent)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (intent.getBooleanExtra(AppLogicConstants.TAG_NEED_RELOAD_DATA, false)) {
            ReloadServerDataService.startActionFullDownload(this, false)
        }

        setupNavigation()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        RuStoreBillingClient.onNewIntent(intent)
    }

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    private fun setupNavigation() {
        bottomNavigationView = findViewById(R.id.nav_view)
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController.configVisibility(binding.navView)
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
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
