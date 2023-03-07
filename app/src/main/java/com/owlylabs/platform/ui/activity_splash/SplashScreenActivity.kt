package com.owlylabs.platform.ui.activity_splash

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import com.owlylabs.platform.billing.BillingClientManager
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.services.ReloadServerDataService
import com.owlylabs.platform.ui.activity_main.MainActivity
import com.owlylabs.platform.ui.activity_start_screen.StartScreenActivity
import com.owlylabs.platform.util.InternetUtil
import com.owlylabs.platform.util.ToastUtil
import dagger.android.support.DaggerAppCompatActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class SplashScreenActivity : DaggerAppCompatActivity() {

    @Inject lateinit var localTabsRepository: AbstractLocalRepository
    @Inject lateinit var billingClientManager: BillingClientManager

    private lateinit var isDataPresentsDisposable : Disposable
    private var networkCallbackToFetchStructure: ConnectivityManager.NetworkCallback? = null
    private var wasInited = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerStructureNetworkCallback()
    }

    override fun onStart() {
        super.onStart()
        isDataPresentsDisposable =
            Observable.combineLatest(
                localTabsRepository.needToKnowIfTabsArePresentObservable(),
                localTabsRepository.getContentTimestampObservable().map { timestamp ->
                    timestamp != 0L
                },
                localTabsRepository.hasAnyActiveSubscription()
            ) { areTabsPresent: Boolean, isTempstampNotEmpty: Boolean, isSubPersist: Boolean ->
                Pair(
                    areTabsPresent && isTempstampNotEmpty,
                    isSubPersist
                )
            }.observeOn(AndroidSchedulers.mainThread()).subscribeBy(
                onError = { ToastUtil.showError(this, it) },
                onNext = {
                    if (it.first){
                        if (!wasInited){
                            wasInited = true
                            unRegisterStructureNetworkCallback()
                            if (it.second){
                                startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
                                finish()
                            }
                            else {
                                startActivity(Intent(this@SplashScreenActivity, StartScreenActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            )
    }

    override fun onStop() {
        super.onStop()
        if (::isDataPresentsDisposable.isInitialized){
            isDataPresentsDisposable.dispose()
        }
    }

    private fun registerStructureNetworkCallback() {
        InternetUtil.registerNetworkCallback(this,
            {
                ReloadServerDataService.startActionFullDownload(this, false)
                ReloadServerDataService.startReloadRecommended(this)
            },
            {
                ToastUtil.showText(this, "The internet connection is lost")
            }
        )?.let {
            networkCallbackToFetchStructure = it
        }
    }

    private fun unRegisterStructureNetworkCallback() {
        networkCallbackToFetchStructure?.let {
            networkCallbackToFetchStructure = null
            InternetUtil.unregisterNetworkCallback(this, it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unRegisterStructureNetworkCallback()
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
