package com.owlylabs.platform.model.repository.local.db.tabs

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.owlylabs.platform.constants.DBConstants
import com.owlylabs.platform.model.data.TabData
import com.owlylabs.platform.model.repository.remote.callbacks.structure.tab.TabCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
abstract class TabDao : BaseDao<TabEntity>() {
    @Query (DBConstants.QUERY_ALL_TABS)
    abstract fun getAllTabsObservable() : Observable<List<TabData>>

    @Query (DBConstants.QUERY_ALL_TABS)
    abstract fun getAllTabsSingle() : Single<List<TabData>>

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert (entity = TabEntity::class)
    abstract fun insertAllTabs(data : List<TabCallback>) : Completable

    @RawQuery
    abstract fun getTabByIdSingle(query : SupportSQLiteQuery) : Single<TabData>

    @RawQuery (observedEntities = [TabEntity::class])
    abstract fun getTabByIdFlowable(query : SupportSQLiteQuery) : Flowable<TabData>

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TabEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<TabCallback>)


    @Query("DELETE FROM TabEntity")
    abstract fun clearThisTable()

    open fun clearAndInsertAllTabsBlocking(remoteData : List<TabCallback>){
        clearThisTable()
        insertOrReplaceBlocking(remoteData)
    }
}