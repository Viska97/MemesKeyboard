package org.visapps.vkmemeskeyboard.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.visapps.vkmemeskeyboard.data.models.Meme

@Dao
interface MemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemes(memes : List<Meme>)

    @Query("SELECT * FROM memes WHERE pack_id = :packId ORDER BY id ASC")
    fun getMemes(packId : Int) : List<Meme>

    @Query("DELETE FROM memes WHERE pack_id = :packId")
    fun deleteByPack(packId : Int)

}