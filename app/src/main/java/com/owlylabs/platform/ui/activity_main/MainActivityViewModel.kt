package com.owlylabs.platform.ui.activity_main

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class MainActivityViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}