package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.account.AccountFragmentEmailDialog
import com.owlylabs.platform.ui.activity_start_screen.enter_name.StartScreenNameFragment
import com.owlylabs.platform.ui.activity_start_screen.subscription.StartScreenSubscriptionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class StartScreenModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeNameFragment(): StartScreenNameFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeSubscriptionFragment(): StartScreenSubscriptionFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAccountFragmentEmailDialog(): AccountFragmentEmailDialog
}