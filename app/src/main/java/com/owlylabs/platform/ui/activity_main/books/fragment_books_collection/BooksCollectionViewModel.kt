package com.owlylabs.platform.ui.books.fragment_books_collection

import androidx.lifecycle.*
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract.BooksCollectionListItemAbstract
import com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract.BooksCollectionListItemSingleBook
import com.owlylabs.platform.ui.books.fragment_books_collection.books_collection_list_item_abstract.BooksCollectionListItemSubtitle
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class BooksCollectionViewModel(repository: AbstractLocalRepository, sectionId: Int) : ViewModel() {

    var sectionData = MediatorLiveData<SectionData>()
    private var hiddenSectionData: LiveData<SectionData>

    var listLiveData = MediatorLiveData<ArrayList<BooksCollectionListItemAbstract>>()
    private var hiddenListLiveData: LiveData<ArrayList<BooksCollectionListItemAbstract>>

    init {
        hiddenSectionData = LiveDataReactiveStreams.fromPublisher(repository.getSectionByIdFlowable(sectionId))
        sectionData.addSource(hiddenSectionData) { newData ->
            val currentValue = sectionData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            currentValue?.let { oldData->
                isOldValuePresents = true
                if (currentValue.compareTo(newData) != 0){
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)){
                sectionData.value = newData
            }
        }

        hiddenListLiveData = LiveDataReactiveStreams.fromPublisher(
            repository
                .getBooksBySectionIdFlowable(sectionId)
                .map { listOfBooks ->
                    val tabListItemAbstract = ArrayList<BooksCollectionListItemAbstract>()
                    val sectionData = repository.getSectionByIdBlocking(sectionId)
                    tabListItemAbstract.add(BooksCollectionListItemSubtitle(sectionData))
                    listOfBooks.forEach { bookd ->
                        tabListItemAbstract.add(BooksCollectionListItemSingleBook(sectionData, bookd))
                    }
                    return@map tabListItemAbstract
                }
        )
        listLiveData.addSource(hiddenListLiveData) { newData ->
            val currentValue = listLiveData.value
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
                listLiveData.value = newData
            }
        }
    }


    class Factory(val repository: AbstractLocalRepository, val sectionId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BooksCollectionViewModel(
                repository,
                sectionId
            ) as T
        }
    }
}
