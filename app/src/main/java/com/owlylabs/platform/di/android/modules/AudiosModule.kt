package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.audios.fragment_audios.AudiosFragment
import com.owlylabs.platform.ui.audios.fragment_audios_collection.AudioCollectionFragment
import com.owlylabs.platform.ui.audios.fragment_audios_detail.AudioDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AudiosModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributAudiosFragment(): AudiosFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAudioDetailFragment(): AudioDetailFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeAudiossCollectionFragment(): AudioCollectionFragment
}