package com.owlylabs.platform.di.java

import com.owlylabs.platform.billing.BillingClientManager
import com.owlylabs.platform.PlatformApplication
import com.owlylabs.platform.di.java.modules.BillingModule
import com.owlylabs.platform.di.java.modules.RepositoryModule
import com.owlylabs.platform.di.java.modules.RetrofitModule
import com.owlylabs.platform.di.java.qualifiers.ApiBaseUrl
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.remote.ServerAPI
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        RetrofitModule::class,
        RepositoryModule::class,
        BillingModule::class
    ]
)
interface CommonComponent {
    fun retrofit() : ServerAPI
    fun repository() : AbstractLocalRepository
    fun billing() : BillingClientManager
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance
            application: PlatformApplication,
            @BindsInstance
            @ApiBaseUrl
            retrofitBaseUrl: String
        ): CommonComponent
    }
}