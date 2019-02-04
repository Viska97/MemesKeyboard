package org.visapps.vkstickerskeyboard.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.visapps.vkstickerskeyboard.data.models.Sticker

@Dao
interface StickerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertStickers(stickers : List<Sticker>)

    @Query("SELECT * FROM stickers WHERE pack_id = :packId ORDER BY id ASC")
    fun getStickers(packId : Int) : List<Sticker>

    @Query("DELETE FROM stickers WHERE pack_id = :packId")
    fun deleteByPack(packId : Int)

}