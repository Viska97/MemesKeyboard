package org.visapps.vkmemeskeyboard.data.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.visapps.vkmemeskeyboard.data.models.Dialog

@Dao
interface DialogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDialogs(dialogs : List<Dialog>) : List<Long>

    @Query("SELECT * FROM dialogs ORDER BY last_message_id DESC")
    fun getDialogs() : DataSource.Factory<Int, Dialog>

    @Query("DELETE FROM dialogs")
    fun deleteDialogs()

}