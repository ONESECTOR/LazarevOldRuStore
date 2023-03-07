package com.owlylabs.platform.ui.tab_with_sections

import androidx.lifecycle.*
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.data.TabData
import com.owlylabs.platform.model.data.tab_list_item_types.*
import com.owlylabs.platform.model.data.tab_list_item_types.audio.TabListItemHorizontalAudioList
import com.owlylabs.platform.model.data.tab_list_item_types.audio.TabListItemSingleAudio
import com.owlylabs.platform.model.data.tab_list_item_types.book.TabListItemHorizontalBookList
import com.owlylabs.platform.model.data.tab_list_item_types.book.TabListItemSingleBook
import com.owlylabs.platform.model.data.tab_list_item_types.TabListItemSingleVideo
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class TabThatRepresentsSectionsViewModel(repository: AbstractLocalRepository, tabId: Int) :
    ViewModel() {

    var tabLiveData = MediatorLiveData<TabData>()
    private var hiddenTabLiveData: LiveData<TabData>

    var listLiveData = MediatorLiveData<ArrayList<TabListItemAbstract>>()
    private var hiddenListLiveData: LiveData<ArrayList<TabListItemAbstract>>

    init {
        hiddenTabLiveData = LiveDataReactiveStreams.fromPublisher(repository.getTabFlowable(tabId))
        tabLiveData.addSource(hiddenTabLiveData) { newValue ->
            val oldValue = tabLiveData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            oldValue?.let { oldData ->
                isOldValuePresents = true
                if (oldData != newValue) {
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)) {
                tabLiveData.value = newValue
            }
        }

        hiddenListLiveData = LiveDataReactiveStreams.fromPublisher(
            repository.getSectionsByTabIdFlowable(tabId)
                .map { t ->
                    val tabListItemAbstract = ArrayList<TabListItemAbstract>()

                    t.forEach { section ->
                        when (section.sectionType) {
                            "book" -> {
                                when (section.typeList) {
                                    0 -> {
                                        val bookList = repository.getBooksBySectionIdBlocking(section.id_section)
                                        if (bookList.size > 0){
                                            checkFotTitleAndSubtitle(section, tabListItemAbstract)
                                            bookList.forEach { book ->
                                                tabListItemAbstract.add(
                                                    TabListItemSingleBook(
                                                        section, book
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    1 -> {
                                        val bookList = repository.getBooksBySectionIdBlocking(section.id_section)
                                        if (bookList.size > 0){
                                            tabListItemAbstract.add(
                                                TabListItemHorizontalBookList(
                                                    section,
                                                    bookList
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            "audio" -> {
                                when (section.typeList) {
                                    0 -> {
                                        val audioList = repository.getAudiosBySectionIdBlocking(section.id_section)
                                        if (audioList.size > 0){
                                            checkFotTitleAndSubtitle(section, tabListItemAbstract)
                                            audioList.forEach { audio ->
                                                tabListItemAbstract.add(
                                                    TabListItemSingleAudio(
                                                        section, audio
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    1 -> {
                                        val audioList = repository.getAudiosBySectionIdBlocking(section.id_section)
                                        if(audioList.size > 0){
                                            tabListItemAbstract.add(
                                                TabListItemHorizontalAudioList(
                                                    section,
                                                    audioList
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            "separator" -> {
                                tabListItemAbstract.add(
                                    TabListItemSeparator(
                                        section
                                    )
                                )
                            }
                            "banner" -> {
                                val bannerList = repository.getBannersBySectionIdBlocking(section.id_section)
                                if (bannerList.size > 0){
                                    tabListItemAbstract.add(TabListItemHorizontalBannerList(section, bannerList))
                                }
                            }
                            "video" -> {
                                when (section.typeList) {
                                    0 -> {
                                        val videoList = repository.getVideosBySectionIdBlocking(section.id_section)
                                        if (videoList.size > 0 ){
                                            checkFotTitleAndSubtitle(section, tabListItemAbstract)
                                            videoList.forEach { video ->
                                                tabListItemAbstract.add(
                                                    TabListItemSingleVideo(
                                                        section, video
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            "text" -> {
                                checkFotTitleAndSubtitle(section, tabListItemAbstract)
                            }
                            "news" -> {
                                when (section.typeList) {
                                    0 -> {
                                        val newsList = repository.getNewsBlocking()
                                        newsList.forEach { news ->
                                            tabListItemAbstract.add(
                                                TabListItemSingleNews(
                                                    section, news
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return@map tabListItemAbstract
                }
        )




        listLiveData.addSource(hiddenListLiveData, Observer { newData ->
            listLiveData.value = newData
        })
    }

    private fun checkFotTitleAndSubtitle(section: SectionData, tabListItemAbstract: ArrayList<TabListItemAbstract>){
        section.title?.let {
            if (!it.isEmpty()){
                tabListItemAbstract.add(
                    TabListItemTitle(
                        section
                    )
                )
            }
        }

        section.subTitle?.let { subTitle ->
            tabListItemAbstract.add(
                TabListItemSubtitle(
                    section
                )
            )
        }
    }


    class Factory(val repository: AbstractLocalRepository, val tabId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TabThatRepresentsSectionsViewModel(
                repository,
                tabId
            ) as T
        }
    }
}