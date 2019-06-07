package org.visapps.vkmemeskeyboard.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.visapps.vkmemeskeyboard.data.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user : User)

    @Query("SELECT * FROM user LIMIT 1")
    fun getUser() : User?

    @Query("DELETE FROM user")
    fun deleteAll()

}