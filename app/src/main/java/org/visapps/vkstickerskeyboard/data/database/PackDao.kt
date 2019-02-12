package org.visapps.vkstickerskeyboard.data.database

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.PackStatus

@Dao
abstract class PackDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertPacks(packs : List<Pack>) : List<Long>

    @Query("SELECT * FROM packs WHERE name LIKE :searchText AND updated = 0 ORDER BY id ASC")
    abstract fun searchPacks(searchText : String) : DataSource.Factory<Int, Pack>

    @Query("SELECT COUNT(*) FROM packs")
    abstract fun packsCount() : Int

    @Query("SELECT * FROM packs WHERE id = :packId")
    abstract fun getPackById(packId : Int) : Pack?

    @Query("SELECT status FROM packs WHERE id = :packId")
    abstract fun getPackStatusById(packId: Int) : Int?

    @Query("SELECT * FROM packs WHERE status = 2")
    abstract fun getSavedPacks() : LiveData<List<Pack>>

    @Query("UPDATE packs SET updated = 0")
    abstract fun invalidateSavedPacks()

    @Query("UPDATE packs SET status = :status WHERE id = :packId")
    abstract fun updatePackStatus(packId : Int, status : Int)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract fun updatePacks(packs : List<Pack>)

    @Query("DELETE FROM packs WHERE status = 0")
    abstract fun deleteNotSavedPacks()

    @Query("DELETE FROM packs WHERE id = :packId")
    abstract fun deletePackById(packId: Int)

    @Transaction
    open fun addPacks(packs: List<Pack>, refresh : Boolean = false) {
        val updateList = ArrayList<Pack>()
        if(refresh) {
            deleteNotSavedPacks()
            invalidateSavedPacks()
        }
        val insertResult = insertPacks(packs)
        for (i in 0 until insertResult.size) {
            if (insertResult[i] == -1L) {
                updateList.add(packs.get(i))
            }
        }
        if (!updateList.isEmpty()) {
            updateList.forEach { it.status = PackStatus.SAVED }
            updatePacks(updateList)
        }
    }

}