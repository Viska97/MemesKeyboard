package org.visapps.vkstickerskeyboard.data.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.visapps.vkstickerskeyboard.data.models.Pack

@Dao
interface PackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts : List<Pack>)

    @Query("SELECT * FROM packs WHERE name LIKE :searchText ORDER BY id ASC")
    fun searchPacks(searchText : String) : DataSource.Factory<Int, Pack>

    @Query("DELETE FROM packs WHERE status = 0")
    fun deleteNotSaved()

}