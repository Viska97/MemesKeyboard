package org.visapps.vkstickerskeyboard.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.visapps.vkstickerskeyboard.data.models.Dialog
import org.visapps.vkstickerskeyboard.data.models.Pack
import org.visapps.vkstickerskeyboard.data.models.Sticker

@Database(entities = [Pack::class, Sticker::class, Dialog::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun packDao(): PackDao
    abstract fun stickerDao(): StickerDao
    abstract fun dialogDao() : DialogDao

    companion object {

        private const val DATABASE_NAME = "stickers.db"

        @Volatile
        private var instance: AppDatabase? = null

        fun get(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
    }

}