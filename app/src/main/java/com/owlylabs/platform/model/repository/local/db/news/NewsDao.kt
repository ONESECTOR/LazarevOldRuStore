package com.owlylabs.platform.model.repository.local.db.news

import androidx.room.*
import com.owlylabs.platform.model.data.NewsData
import com.owlylabs.platform.model.repository.remote.callbacks.content.news.NewsCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable

@Dao
abstract class NewsDao : BaseDao<NewsEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = NewsEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<NewsCallback>)

    @Query("SELECT * FROM NewsEntity")
    abstract fun getNews() : Flowable<List<NewsData>>

    @Query("SELECT * FROM NewsEntity WHERE id = :newsId LIMIT 1")
    abstract fun getNewsByIdFlowable(newsId: Int) : Flowable<NewsData>

    @Delete (entity = NewsEntity::class)
    abstract fun deleteBlocking(list: List<NewsCallback>)

    @Query("SELECT * FROM NewsEntity")
    abstract fun getNewsBySectionIdBlocking() : List<NewsData>
}