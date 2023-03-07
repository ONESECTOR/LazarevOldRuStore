package com.owlylabs.platform.ui.books.fragment_books

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.owlylabs.platform.model.repository.local.AbstractLocalRepository

class BooksViewModel(repository: AbstractLocalRepository, tabId: Int) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    class Factory(val repository: AbstractLocalRepository, val tabId: Int) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return BooksViewModel(
                repository,
                tabId
            ) as T
        }
    }
}