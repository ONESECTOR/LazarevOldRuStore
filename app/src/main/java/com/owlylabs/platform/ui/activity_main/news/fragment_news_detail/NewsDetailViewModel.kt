package com.owlylabs.platform.ui.news.fragment_news_detail

import androidx.lifecycle.*
import com.owlylabs.platform.model.data.JumpData
import com.owlylabs.platform.model.data.NewsData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class NewsDetailViewModel(repository: AbstractLocalRepository, newsId: Int, argDoNotWaitMenuAnimation: Int) : ViewModel() {
    var newsLiveData: LiveData<NewsData> private set
    var jumpLiveData = MediatorLiveData<JumpData>()

    val isTheBottomNavigationViewClosed = MutableLiveData(argDoNotWaitMenuAnimation == 1)

    init {
        repository.getNewsByIdFlowable(newsId)
        newsLiveData = LiveDataReactiveStreams.fromPublisher(repository.getNewsByIdFlowable(newsId))
        jumpLiveData.addSource(newsLiveData) { newsInfo ->
            isTheBottomNavigationViewClosed.value?.let { isReady ->
                if (isReady){
                    val jumpType = newsInfo.jump_type.toInt()
                    if (jumpType != 0){
                        jumpLiveData.value = JumpData(jumpType, newsInfo.jump)
                    }
                }
            }
        }
        jumpLiveData.addSource(isTheBottomNavigationViewClosed) { isReady ->
            if (isReady){
                newsLiveData.value?.let {  newsInfo ->
                    val jumpType = newsInfo.jump_type.toInt()
                    if (jumpType != 0){
                        jumpLiveData.value = JumpData(jumpType, newsInfo.jump)
                    }
                }
            }
        }
    }


    class Factory(val repository: AbstractLocalRepository, val tabId: Int, val argDoNotWaitMenuAnimation: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NewsDetailViewModel(
                repository,
                tabId,
                argDoNotWaitMenuAnimation
            ) as T
        }
    }
}