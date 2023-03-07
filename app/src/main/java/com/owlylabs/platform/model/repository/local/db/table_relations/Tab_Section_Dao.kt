package com.owlylabs.platform.model.repository.local.db.table_relations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.owlylabs.platform.model.repository.local.db.BaseDao

@Dao
abstract class Tab_Section_Dao : BaseDao<Tab_Section>() {

    @Insert()
    abstract fun insertOrReplaceBlocking(list: List<Tab_Section>)

    @Insert()
    abstract fun insertOrReplaceBlocking(list: Tab_Section)

    @Query("DELETE FROM Tab_Section")
    abstract fun clearThisTable()
}