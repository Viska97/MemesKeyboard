package org.visapps.vkstickerskeyboard.data.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.coroutineScope
import org.visapps.vkstickerskeyboard.data.models.Pack

@Dao
interface PackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPacks(packs : List<Pack>) : List<Long>

    @Query("SELECT * FROM packs")
    fun searchPacks() : DataSource.Factory<Int, Pack>

    @Query("DELETE FROM packs WHERE status = 0")
    fun deleteNotSaved()

}