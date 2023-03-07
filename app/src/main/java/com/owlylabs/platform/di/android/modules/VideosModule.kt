package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.videos.fragment_videos.VideosFragment
import com.owlylabs.platform.ui.videos.fragment_videos_detail.VideoDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class VideosModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeVideosFragment(): VideosFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeVideosDetailFragment(): VideoDetailFragment
}