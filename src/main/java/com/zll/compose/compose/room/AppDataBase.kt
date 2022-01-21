package com.zll.compose.compose.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SearchResultModel::class], version = 1,exportSchema = false)
abstract class AppDataBase : RoomDatabase() {


    abstract fun searchDao(): SearchDao

    companion object {
        private val DB_NAME = "compose.db"
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    DB_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}