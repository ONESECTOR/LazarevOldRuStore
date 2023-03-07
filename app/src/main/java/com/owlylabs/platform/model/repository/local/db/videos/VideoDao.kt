package com.owlylabs.platform.model.repository.local.db.videos

import androidx.room.*
import com.owlylabs.platform.model.data.VideoData
import com.owlylabs.platform.model.repository.remote.callbacks.content.videos.VideoCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class VideoDao : BaseDao<VideoEntity>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = VideoEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<VideoCallback>)

    @Delete(entity = VideoEntity::class)
    abstract fun deleteBlocking(list: List<VideoCallback>)

    @Query("SELECT * FROM VideoEntity AS v INNER JOIN Section_Item AS s_b ON v.id_book = s_b.itemId WHERE s_b.sectionId = :sectionId")
    abstract fun getVideosBySectionIdBlocking(sectionId: Int) : List<VideoData>

    @Query("SELECT * FROM VideoEntity WHERE id_book = :videoId")
    abstract fun getVideosById(videoId: Int) : Flowable<VideoData>

    @Query("SELECT name_book FROM VideoEntity WHERE id_book = :videoId")
    abstract fun getVideoNameByVideoIdSingle(videoId: Int) : Single<String>

}