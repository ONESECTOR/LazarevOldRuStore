package com.owlylabs.platform.ui.books.fragment_books_detail

import androidx.lifecycle.*
import com.owlylabs.epublibrary.OwlyReader
import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class BookDetailViewModel(repository: AbstractLocalRepository, bookId: Int) : ViewModel() {
    protected var hiddenBookLiveData: LiveData<BookData> private set
    var bookLiveData = MediatorLiveData<BookData>()

    protected var hiddenBookProgressLiveData: LiveData<Int> private set
    var bookProgressLiveData = MediatorLiveData<Int>()

    init {
        hiddenBookLiveData =
            LiveDataReactiveStreams.fromPublisher(repository.getBookByIdFlowable(bookId))
        bookLiveData.addSource(hiddenBookLiveData) { newData ->
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
        }

        hiddenBookProgressLiveData =
            LiveDataReactiveStreams.fromPublisher(OwlyReader.getBookProgressFlowable(bookId))
        bookProgressLiveData.addSource(hiddenBookProgressLiveData) { progressNewValue ->
            val currentValue = bookProgressLiveData.value
            var isOldValuePresents = false
            var isOldValueDiffersFromNews = false
            currentValue?.let { oldData ->
                isOldValuePresents = true
                if (oldData != progressNewValue) {
                    isOldValueDiffersFromNews = true
                }
            }
            if ((!isOldValuePresents) || (isOldValuePresents && isOldValueDiffersFromNews)) {
                bookProgressLiveData.value = progressNewValue
            }
        }
    }


    class Factory(val repository: AbstractLocalRepository, val bookId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BookDetailViewModel(
                repository,
                bookId
            ) as T
        }
    }
}
