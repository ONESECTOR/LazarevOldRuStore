package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.activity_main.subscription.SubscriptionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class SubscriptionModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeSubscriptionFragment(): SubscriptionFragment

}