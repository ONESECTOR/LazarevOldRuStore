package com.owlylabs.platform.model.repository.local.db.recommended

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.owlylabs.platform.model.data.RecommendedData
import com.owlylabs.platform.model.repository.remote.callbacks.recommended.RecommendedBanner
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable

@Dao
abstract class RecommendedDao : BaseDao<RecommendedBannerEntity>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = RecommendedBannerEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<RecommendedBanner>)

    @Query("DELETE FROM RecommendedBannerEntity")
    abstract fun clearThisTableBlocking()

    @Query("SELECT * FROM RecommendedBannerEntity")
    abstract fun getRecommendedFlowable(): Flowable<List<RecommendedData>>
}