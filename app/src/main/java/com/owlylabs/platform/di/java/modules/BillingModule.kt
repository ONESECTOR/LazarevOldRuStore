package com.owlylabs.platform.di.java.modules

import com.owlylabs.platform.billing.BillingClientManager
import com.owlylabs.platform.PlatformApplication
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.remote.ServerAPI
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BillingModule {

    @Singleton
    @Provides
    fun provideBillingClientManager(
        appContext: PlatformApplication,
        service: ServerAPI,
        localRepository: AbstractLocalRepository
    ) : BillingClientManager {
        return BillingClientManager(appContext, service, localRepository)
    }
}