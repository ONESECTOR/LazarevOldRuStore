package com.owlylabs.platform.model.repository.local.db.books

import androidx.room.*
import com.owlylabs.platform.model.data.BookData
import com.owlylabs.platform.model.repository.remote.callbacks.content.books.BookCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class BookDao : BaseDao<BookEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = BookEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<BookCallback>)

    @Delete (entity = BookEntity::class)
    abstract fun deleteBlocking(list: List<BookCallback>)

    @Query("SELECT b.* FROM Section_Item AS s_b INNER JOIN BookEntity AS b ON s_b.itemId = b.idBook WHERE s_b.sectionId = :sectionId ORDER BY s_b.id")
    abstract fun getBooksBySectionIdBlocking(sectionId: Int) : List<BookData>

    @Query("SELECT * FROM BookEntity AS b INNER JOIN Section_Item AS s_b ON b.idBook = s_b.itemId WHERE s_b.sectionId = :sectionId")
    abstract fun getBooksBySectionIFlowable(sectionId: Int) : Flowable<List<BookData>>

    @Query("SELECT * FROM BookEntity WHERE idBook = :bookId")
    abstract fun getBookById(bookId: Int) : Flowable<BookData>

    @Query("SELECT nameBook FROM BookEntity WHERE idBook = :bookId")
    abstract fun getBookNameByBookIdSingle(bookId: Int) : Single<String>

    @Query("SELECT * FROM BookEntity WHERE idBook = :bookId")
    abstract fun getBookByIdBlocking(bookId: Int) : BookData
}