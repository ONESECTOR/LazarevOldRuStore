package com.owlylabs.platform.ui.audios.fragment_audios_detail

import androidx.lifecycle.*
import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class AudioDetailViewModel(repository: AbstractLocalRepository, audioBookId: Int) : ViewModel() {
    var hiddenBookLiveData: LiveData<AudioData> private set
    var bookLiveData = MediatorLiveData<AudioData>()

    init {
        hiddenBookLiveData =
            LiveDataReactiveStreams.fromPublisher(repository.getAudioByIdFlowable(audioBookId))
        bookLiveData.addSource(hiddenBookLiveData, Observer { newData ->
            val currentValue = bookLiveData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            currentValue?.let { oldData ->
                isOldValuePresents = true
                if (oldData.compareTo(newData) != 0) {
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)) {
                bookLiveData.value = newData
            }
        })
    }


    class Factory(val repository: AbstractLocalRepository, val audioBookId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AudioDetailViewModel(
                repository,
                audioBookId
            ) as T
        }
    }
}
