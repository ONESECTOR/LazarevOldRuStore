package com.owlylabs.platform.di.android

import com.owlylabs.platform.billing.BillingClientManager
import com.owlylabs.platform.di.android.modules.ServiceBindingModule
import com.owlylabs.platform.PlatformApplication
import com.owlylabs.platform.di.android.modules.ActivityBindingModule
import com.owlylabs.platform.di.java.qualifiers.ApiBaseUrl
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.remote.ServerAPI
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Main component of the app, created and persisted in the Application class.
 *
 * Whenever a new module is created, it should be added to the list of modules.
 * [AndroidSupportInjectionModule] and [AndroidInjectionModule] are the modules from Dagger.Android
 * that help with the generation and location of subComponents.
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        ServiceBindingModule::class]
)
interface AppComponent : AndroidInjector<PlatformApplication> {
    @Component.Factory
    interface Factory {
        /**
         * @param application has [BindInstance] annotation.
         * This annotation is used for removing constructor from module and chaining modules where
         * we get component.
         *
         * @param retrofitBaseUrl has [ApiBaseUrl] annotation.
         * It is a custom [Qualifier].
         * Qualifiers are used to distinguish between objects of the same type but with different
         * instances.
         *
         * JavaX has a default qualifier [Named], where we can pass the name of object's instance.
         */
        fun create(
            @BindsInstance
            application: PlatformApplication,
            @BindsInstance
            retrofit: ServerAPI,
            @BindsInstance
            repository: AbstractLocalRepository,
            @BindsInstance
            billingClientManager: BillingClientManager
        ): AppComponent
    }
}