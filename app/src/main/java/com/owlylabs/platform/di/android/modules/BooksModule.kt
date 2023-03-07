package com.owlylabs.platform.di.android.modules

import com.owlylabs.platform.di.android.scopes.FragmentScoped
import com.owlylabs.platform.ui.books.fragment_books.BooksFragment
import com.owlylabs.platform.ui.books.fragment_books_collection.BooksCollectionFragment
import com.owlylabs.platform.ui.books.fragment_books_detail.BookDetailFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BooksModule {
    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeBooksFragment(): BooksFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeBooksDetailFragment(): BookDetailFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeBooksCollectionFragment(): BooksCollectionFragment
}