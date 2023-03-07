package com.owlylabs.platform.model.repository.local.db.udid

import androidx.room.*
import com.owlylabs.platform.model.data.HashData
import com.owlylabs.platform.model.repository.remote.callbacks.hash.HashCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao

@Dao
abstract class HashDao : BaseDao<HashEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert(entity = HashEntity::class)
    abstract fun insertBlocking(udid: HashCallback)

    @Query("SELECT * FROM HashEntity")
    abstract fun getHashBlocking() : HashData?
}