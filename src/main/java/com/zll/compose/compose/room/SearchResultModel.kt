package com.zll.compose.compose.room

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_result")
data class SearchResultModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,

    @NonNull
    @ColumnInfo(name = "search_title")
    val title: String,

    @NonNull
    @ColumnInfo(name = "search_time")
    val time: Long,

    )