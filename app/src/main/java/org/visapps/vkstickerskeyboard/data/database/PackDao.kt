package org.visapps.vkstickerskeyboard.data.database

import androidx.lifecycle.LiveData
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

    @Query("SELECT * FROM packs WHERE name LIKE :searchText AND updated = 1 ORDER BY id ASC")
    fun searchPacks(searchText : String) : DataSource.Factory<Int, Pack>

    @Query("SELECT COUNT(*) FROM packs")
    fun packsCount() : Int

    @Query("SELECT * FROM packs WHERE id = :packId")
    fun getPackById(packId : Int) : Pack?

    @Query("SELECT status FROM packs WHERE id = :packId")
    fun getPackStatusById(packId: Int) : Int?

    @Query("SELECT * FROM packs WHERE status = 2")
    fun getSavedPacks() : LiveData<List<Pack>>

    @Query("DELETE FROM packs WHERE status = 0")
    fun deleteNotSaved()

}