package com.owlylabs.platform.model.repository.local.db.banners

import androidx.room.*
import com.owlylabs.platform.model.data.BannerData
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.repository.remote.callbacks.content.banners.BannerCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Maybe

@Dao
abstract class BannerDao : BaseDao<BannerEntity>() {

    /**
     * When there is no data, Single will trigger onError(EmptyResultSetException.class)
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = BannerEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<BannerCallback>)

    @Delete (entity = BannerEntity::class)
    abstract fun deleteBlocking(list: List<BannerCallback>)

    @Query("SELECT b.* FROM BannerEntity AS b INNER JOIN Section_Item AS s_b ON b.id = s_b.itemId WHERE s_b.sectionId = :sectionId")
    abstract fun getBannersBySectionIdBlocking(sectionId: Int) : List<BannerData>

    @Query("SELECT CASE WHEN (SELECT COUNT(*) FROM BookEntity WHERE idBook =:id) != 0 THEN 1 WHEN (SELECT COUNT (*) FROM AudioEntity WHERE idBook =:id) != 0 THEN 2 WHEN (SELECT COUNT(*) FROM VideoEntity WHERE id_book =:id) != 0 THEN 3 END")
    abstract fun getBannerActionType(id: Int) : Maybe<Int>

    @Query("SELECT * FROM SectionEntity WHERE id_section = :sectionId LIMIT 1")
    abstract fun getSectionByIdMaybe(sectionId: Int): Maybe<SectionData>
}