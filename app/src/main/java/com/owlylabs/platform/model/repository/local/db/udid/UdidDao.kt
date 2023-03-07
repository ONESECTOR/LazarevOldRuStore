package com.owlylabs.platform.model.repository.local.db.udid

import androidx.room.*
import com.owlylabs.platform.model.data.UdidData
import com.owlylabs.platform.model.repository.remote.callbacks.udid.UdidCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao

@Dao
abstract class UdidDao : BaseDao<UdidEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert(entity = UdidEntity::class)
    abstract fun insertBlocking(udid: UdidCallback)

    @Query("SELECT * FROM UdidEntity")
    abstract fun getUdidBlocking() : UdidData?
}