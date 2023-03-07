package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.ActivityScoped
import com.owlylabs.platform.ui.activity_main.MainActivity
import com.owlylabs.platform.ui.activity_splash.SplashScreenActivity
import com.owlylabs.platform.ui.activity_start_screen.StartScreenActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector()
    internal abstract fun splashActivity(): SplashScreenActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            StartScreenModule::class
        ]
    )
    internal abstract fun startScreenActivity(): StartScreenActivity

    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            NewsModule::class,
            BooksModule::class,
            AudiosModule::class,
            VideosModule::class,
            AccountModule::class
        ]
    )
    internal abstract fun mainActivity(): MainActivity
}