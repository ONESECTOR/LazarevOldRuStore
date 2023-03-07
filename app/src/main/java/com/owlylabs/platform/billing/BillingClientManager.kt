/*
 * Copyright 2018 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.owlylabs.platform.billing

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.android.billingclient.api.*
import com.owlylabs.platform.PlatformApplication
import com.owlylabs.platform.constants.BillingConstants
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.local.db.subs.SubscriptionEntity
import com.owlylabs.platform.model.repository.remote.ServerAPI
import com.owlylabs.platform.util.InternetUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BillingClientManager @Inject constructor(
    private val appContext: PlatformApplication,
    private val service: ServerAPI,
    private val localRepository: AbstractLocalRepository
) : LifecycleObserver {

    val skusWithSkuDetails = MutableLiveData<ArrayList<SkuDetails>>()

    private lateinit var billingClient: BillingClient
    private var compositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun createBilling() {
        compositeDisposable = CompositeDisposable()
        billingClient = BillingClient.newBuilder(appContext.applicationContext)
            .setListener { billingResult, purchases ->
                val responseCode = billingResult.responseCode
                if (responseCode == BillingClient.BillingResponseCode.OK) {
                    processPurchases(purchases)
                }
            }
            .enablePendingPurchases() // Not used for subscriptions.
            .build()
        if (!billingClient.isReady) {
            startServiceConnection {
                querySkuDetails {
                    val result = billingClient.queryPurchases(BillingClient.SkuType.SUBS)
                    result.purchasesList?.let {
                        processPurchases(it)
                    } ?: kotlin.run {
                        processPurchases(null)
                    }
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyBilling() {
        if (::billingClient.isInitialized) {
            billingClient.endConnection()
        }
        compositeDisposable.dispose()
    }

    fun restart() {
        destroyBilling()
        createBilling()
    }

    private fun startServiceConnection(executeOnSuccess: Runnable) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {

            }

            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    executeOnSuccess.run()
                }
            }
        })
    }

    private fun querySkuDetails(runnableDoOnSkuLoaded: Runnable) {
        val params = SkuDetailsParams.newBuilder()
            .setType(BillingClient.SkuType.SUBS)
            .setSkusList(
                listOf(
                    BillingConstants.MONTH,
                    BillingConstants.YEAR
                )
            ).build()

        billingClient.querySkuDetailsAsync(
            params
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                skuDetailsList?.let {
                    val finalList = ArrayList<SkuDetails>(it.size)
                    for (details in it) {
                        when (details.sku) {
                            BillingConstants.MONTH -> finalList.add(0, details)
                            BillingConstants.YEAR -> finalList.add(1, details)
                        }
                    }
                    skusWithSkuDetails.postValue(finalList)
                    runnableDoOnSkuLoaded.run()
                }
            }
        }
    }

    private fun processPurchases(purchasesList: List<Purchase>?) {
        purchasesList?.let {
            it.forEach { purchase ->
                if (InternetUtil.isNetworkConnected(appContext)) {
                    compositeDisposable.add(
                        service.getSubscriptionInfo(
                            packageName = purchase.packageName,
                            productId = purchase.skus[0],
                            token = purchase.purchaseToken
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeBy(
                                onError = {},
                                onSuccess = {
                                    compositeDisposable.add(localRepository.saveSubcription(
                                        SubscriptionEntity(it.product_id, it.expiryTimeMillis)
                                    ).subscribe())
                                    if (!purchase.isAcknowledged) {
                                        val params = AcknowledgePurchaseParams.newBuilder()
                                            .setPurchaseToken(purchase.purchaseToken)
                                            .build()
                                        billingClient.acknowledgePurchase(params) {}
                                    }
                                }
                            )
                    )
                }
            }
        }
    }

    fun launchBillingFlow(activity: Activity, params: BillingFlowParams) {
        billingClient.launchBillingFlow(activity, params)
    }
}
