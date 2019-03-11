package org.visapps.vkmemeskeyboard.data.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import org.visapps.vkmemeskeyboard.data.models.Pack

@Dao
interface PackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertPacks(packs : List<Pack>) : List<Long>

    @Query("SELECT * FROM packs WHERE name LIKE :searchText AND updated = 0 ORDER BY id ASC")
    fun searchPacks(searchText : String) : DataSource.Factory<Int, Pack>

    @Query("SELECT COUNT(*) FROM packs WHERE updated = 0")
    fun updatedPacksCount() : Int

    @Query("SELECT * FROM packs WHERE id = :packId")
    fun getPackById(packId : Int) : Pack?

    @Query("SELECT status FROM packs WHERE id = :packId")
    fun getPackStatusById(packId: Int) : Int?

    @Query("SELECT * FROM packs WHERE status = 2")
    fun getSavedPacks() : LiveData<List<Pack>>

    @Query("UPDATE packs SET updated = 1")
    fun invalidatePacks()

    @Query("UPDATE packs SET updated = 0 WHERE id in (:packsIds)")
    fun updateSavedPacks(packsIds : List<Int>)

    @Query("UPDATE packs SET status = :status WHERE id = :packId")
    fun updatePackStatus(packId : Int, status : Int)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun updatePacks(packs : List<Pack>)

    @Query("DELETE FROM packs WHERE status = 0")
    fun deleteNotSavedPacks()

    @Query("DELETE FROM packs WHERE id = :packId")
    fun deletePackById(packId: Int)

}