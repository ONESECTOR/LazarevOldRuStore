package com.owlylabs.platform.ui.news.fragment_news

import androidx.lifecycle.*
import com.owlylabs.platform.model.data.NewsData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class NewsViewModel(repository: AbstractLocalRepository, tabId: Int) : ViewModel() {

    var newsLiveData = MediatorLiveData<List<NewsData>>()
    var sectionData = MediatorLiveData<SectionData>()
    private var hiddenNewsPublisher: LiveData<List<NewsData>>
    private var hiddenSectionData: LiveData<SectionData>

    init {
        hiddenSectionData = LiveDataReactiveStreams.fromPublisher(repository.getSectionByIdFlowable(tabId))
        hiddenNewsPublisher = LiveDataReactiveStreams.fromPublisher(repository.getNews())
        newsLiveData.addSource(hiddenNewsPublisher) { newData ->
            val currentValue = newsLiveData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            currentValue?.let { oldData->
                isOldValuePresents = true
                if (oldData.size != newData.size){
                    isOldValueDiffersFromNews = true
                } else {
                    oldData.forEachIndexed { index, value ->
                        if (value.compareTo(newData.get(index)) != 0){
                            isOldValueDiffersFromNews = true
                            return@forEachIndexed
                        }
                    }
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)){
                newsLiveData.value = newData
            }
        }
        sectionData.addSource(hiddenSectionData) { newData ->
            val currentValue = sectionData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            currentValue?.let { oldData ->
                isOldValuePresents = true
                if (oldData.compareTo(newData) != 0){
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)){
                sectionData.value = newData
            }
        }

    }

    class Factory(val repository: AbstractLocalRepository, val tabId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return NewsViewModel(
                repository,
                tabId
            ) as T
        }
    }
}