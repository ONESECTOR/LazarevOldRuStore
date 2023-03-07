package com.owlylabs.platform.model.repository.local.db.sections

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.owlylabs.platform.model.data.SectionData
import com.owlylabs.platform.model.repository.remote.callbacks.structure.section.SectionCallback
import com.owlylabs.platform.model.repository.local.db.BaseDao
import io.reactivex.Flowable

@Dao
abstract class SectionDao : BaseDao<SectionEntity>() {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SectionEntity::class)
    abstract fun insertOrReplaceBlocking(list: List<SectionCallback>)

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = SectionEntity::class)
    abstract fun insertOrReplaceBlocking(list: SectionCallback)

    @Query("DELETE FROM SectionEntity")
    abstract fun clearThisTable()

    @Query("SELECT s.* FROM SectionEntity AS s INNER JOIN TAB_SECTION AS t_s ON s.id_section = t_s.sectionId WHERE t_s.tabId = :tabId")
    abstract fun getSectionsByTabIdFlowable(tabId: Int): Flowable<List<SectionData>>

    @Query("SELECT * FROM SectionEntity WHERE id_section = :sectionId LIMIT 1")
    abstract fun getSectionByIdFlowable(sectionId: Int): Flowable<SectionData>

    @Query("SELECT * FROM SectionEntity WHERE id_section = :sectionId LIMIT 1")
    abstract fun getSectionByIdBlocking(sectionId: Int): SectionData

}