package com.owlylabs.platform.ui.videos.fragment_videos_detail

import androidx.lifecycle.*
import com.owlylabs.platform.model.data.VideoData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class VideoDetailViewModel(repository: AbstractLocalRepository, videoId: Int) : ViewModel() {
    var hiddenVideoLiveData: LiveData<VideoData> private set
    var videoLiveData = MediatorLiveData<VideoData>()

    init {
        hiddenVideoLiveData =
            LiveDataReactiveStreams.fromPublisher(repository.getVideoByIdFlowable(videoId))
        videoLiveData.addSource(hiddenVideoLiveData, Observer { newData ->
            val currentValue = videoLiveData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            currentValue?.let { oldData ->
                isOldValuePresents = true
                if (oldData.compareTo(newData) != 0) {
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)) {
                videoLiveData.value = newData
            }
        })
    }


    class Factory(val repository: AbstractLocalRepository, val videoId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return VideoDetailViewModel(
                repository,
                videoId
            ) as T
        }
    }
}
