package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.news.fragment_news.NewsFragment
import com.owlylabs.platform.ui.news.fragment_news_detail.NewsDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NewsModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeNewsFragment(): NewsFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeNewsDetailFragment(): NewsDetailFragment
}