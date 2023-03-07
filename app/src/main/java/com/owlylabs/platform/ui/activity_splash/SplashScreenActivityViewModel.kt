package com.owlylabs.platform.ui.activity_splash

import android.app.Application
import androidx.lifecycle.*
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository
import com.owlylabs.platform.model.repository.remote.ServerAPI
import io.reactivex.disposables.CompositeDisposable

class SplashScreenActivityViewModel(
    application: Application,
    abstractLocalRepository: AbstractLocalRepository,
    api: ServerAPI
) : AndroidViewModel(application) {
    private val isReady = MutableLiveData(false)
    private val compositeDisposable = CompositeDisposable()

    fun iSReadyToStart(): LiveData<Boolean> {
        return isReady
    }

    class Factory(
        val application: Application,
        val abstractLocalRepository: AbstractLocalRepository,
        val api: ServerAPI
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashScreenActivityViewModel(
                application,
                abstractLocalRepository,
                api
            ) as T
        }
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}