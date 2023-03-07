package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.account.AccountFragment
import com.owlylabs.platform.ui.account.AccountFragmentEmailDialog
import com.owlylabs.platform.ui.account.AccountFragmentNameDialog
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AccountModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAccountFragment(): AccountFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAccountFragmentNameDialog(): AccountFragmentNameDialog

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAccountFragmentEmailDialog(): AccountFragmentEmailDialog
}