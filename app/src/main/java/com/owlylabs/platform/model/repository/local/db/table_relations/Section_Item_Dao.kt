package com.owlylabs.platform.model.repository.local.db.table_relations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.owlylabs.platform.model.repository.local.db.BaseDao

@Dao
abstract class Section_Item_Dao : BaseDao<Section_Item>() {

    @Insert()
    abstract fun insertOrReplaceBlocking(list: List<Section_Item>)

    @Insert()
    abstract fun insertOrReplaceBlocking(list: Section_Item)

    @Query("DELETE FROM Section_Item")
    abstract fun clearThisTable()

}