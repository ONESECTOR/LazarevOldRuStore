package com.owlylabs.platform.model.repository.local.db

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single

@Dao
abstract class BaseDao<T> {

    /**
     * Where you call subscribeOn() in a chain doesn't really matter. Where you call observeOn() does matter.
     *
     * subscribeOn() tells the whole chain which thread to start processing on. You should only call it once per chain.
     * If you call it again lower down the stream it will have no effect.
     *
     * observeOn() causes all operations which happen below it to be executed on the specified scheduler.
     * You can call it multiple times per stream to move between different threads.
     */


    /**
     * In case of error inserting the data, Completable, Single and Maybe will emit the exception in onError.
     */

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrReplace(t: T) : Completable

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrReplace(list: List<T>) : Completable

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrIgnore(t: T) : Completable

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrIgnore(list: List<T>) : Completable

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrIgnoreAsSingle(list: List<T>) : Single<List<Long>>

    /**
     * the value emitted on onSuccess is the list of row ids of the items inserted
     */
    @Insert
    abstract fun insertAndReturnRowId(list: List<T>): Single<List<Long>>

    /**
     * the value emitted on onSuccess is the row id of the item inserted
     */
    @Insert
    abstract fun insertAndReturnRowId(t: T): Single<Long>

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Delete
    abstract fun delete(t: T) : Completable

    /**
     * onComplete is called as soon as the insertion was done
     */
    @Delete
    abstract fun delete(list: List<T>) : Completable
}