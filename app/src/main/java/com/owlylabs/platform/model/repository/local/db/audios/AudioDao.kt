package com.owlylabs.platform.model.repository.local.db.audios

import androidx.room.*
import com.owlylabs.platform.model.data.AudioData
import com.owlylabs.platform.model.repository.remote.callbacks.content.audios.AudioBookCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class AudioDao : BaseDao<AudioEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = AudioEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<AudioBookCallback>)

    @Delete (entity = AudioEntity::class)
    abstract fun deleteBlocking(list: List<AudioBookCallback>)

    @Query("SELECT * FROM AudioEntity AS a INNER JOIN Section_Item AS s_b ON a.idBook = s_b.itemId WHERE s_b.sectionId = :sectionId")
    abstract fun getAudiosBySectionIdBlocking(sectionId: Int) : List<AudioData>

    @Query("SELECT * FROM AudioEntity AS a INNER JOIN Section_Item AS s_b ON a.idBook = s_b.itemId WHERE s_b.sectionId = :sectionId")
    abstract fun getAudiosBySectionIdFlowable(sectionId: Int) : Flowable<List<AudioData>>

    @Query("SELECT * FROM AudioEntity WHERE idBook = :audioId")
    abstract fun getAudioByIdFlowable(audioId: Int) : Flowable<AudioData>

    @Query("SELECT * FROM AudioEntity WHERE idBook = :audioId")
    abstract fun getAudioByIdBlocking(audioId: Int) : AudioData

    @Query("SELECT nameBook FROM AudioEntity WHERE idBook = :audioId")
    abstract fun getAudioNameByAudioIdSingle(audioId: Int) : Single<String>
}