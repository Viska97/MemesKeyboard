package org.visapps.vkmemeskeyboard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.visapps.vkmemeskeyboard.data.models.Dialog
import org.visapps.vkmemeskeyboard.data.models.Pack
import org.visapps.vkmemeskeyboard.data.models.Meme
import org.visapps.vkmemeskeyboard.data.models.User

@Database(entities = [Pack::class, Meme::class, Dialog::class, User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun packDao(): PackDao
    abstract fun stickerDao(): MemeDao
    abstract fun dialogDao() : DialogDao
    abstract fun userDao() : UserDao

    companion object {

        private const val DATABASE_NAME = "memes.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance
                    ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }

}