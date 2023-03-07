package com.owlylabs.platform.model.repository.local.db.content_timestamp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.owlylabs.platform.model.repository.local.db.content_timestamp.ContentTimestampEntity
import com.owlylabs.platform.model.repository.remote.callbacks.content.ContentCallback
import com.owlylabs.platform.model.repository.remote.callbacks.content.audios.AudioBookCallback
import io.reactivex.Observable
import io.reactivex.Single
import java.util.*

@Dao
abstract class ContentTimestampDao {
    @Insert(entity = ContentTimestampEntity::class, onConflict = OnConflictStrategy.REPLACE )
    abstract fun update(timeStamp : ContentTimestampEntity)

    @Query("DELETE FROM ContentTimestampEntity")
    abstract fun clearThisTable()

    fun updateBlocking(timeStamp : ContentTimestampEntity){
        clearThisTable()
        update(timeStamp)
    }

    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN lastTimestamp ELSE 0 END FROM ContentTimestampEntity")
    abstract fun getLastTimeStampBlocking() : Long

    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN lastTimestamp ELSE 0 END FROM ContentTimestampEntity")
    abstract fun getLastTimeStampObservable() : Observable<Long>

    @Query("SELECT CASE WHEN COUNT(1) > 0 THEN lastTimestamp ELSE 0 END FROM ContentTimestampEntity")
    abstract fun getLastTimeStampSingle() : Single<Long>
}