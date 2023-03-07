package com.owlylabs.platform.ui.activity_start_screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.owlylabs.platform.billing.BillingClientManager
import com.owlylabs.platform.R
import com.owlylabs.platform.databinding.ActivityStartScreenBinding
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.ui.activity_main.MainActivity
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class StartScreenActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var localTabsRepository: AbstractLocalRepository

    @Inject
    lateinit var billingClientManager: BillingClientManager

    private val compositeDisposableOnCreate = CompositeDisposable()

    private lateinit var binding: ActivityStartScreenBinding

    override fun onStart() {
        super.onStart()
        overridePendingTransition(0, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configNavigation()
    }

    private fun configNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.start_screen_navigation)
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val isFirstLaunch = sharedPreference.getBoolean("first_launch",true)

        if (intent.extras?.getBoolean("is_from_app", false) == true) {
            graph.startDestination = R.id.startScreenSubscriptionFragment
        } else {
            if (isFirstLaunch) {
                graph.startDestination = R.id.startScreenNameFragment
            } else {
                graph.startDestination = R.id.startScreenNameFragment
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
        navHostFragment.navController.graph = graph
    }

    override fun onDestroy() {
        compositeDisposableOnCreate.dispose()
        super.onDestroy()
    }
}
