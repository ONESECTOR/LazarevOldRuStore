package com.owlylabs.platform.model.repository.local.db.account

import androidx.room.*
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable

@Dao
abstract class AccountDao : BaseDao<AccountEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Query("SELECT name FROM AccountEntity")
    abstract fun getAccountName() : Flowable<String>
}