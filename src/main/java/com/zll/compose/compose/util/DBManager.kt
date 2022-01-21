package com.zll.compose.compose.util

import com.zll.compose.App
import com.zll.compose.compose.room.AppDataBase
import com.zll.compose.compose.room.SearchDao

object DBManager {

    fun searchDao(): SearchDao {
        return App.CONTEXT.database.searchDao()
    }
}