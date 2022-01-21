package com.zll.compose.compose.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {

    @Query("SELECT * FROM search_result ORDER BY search_time DESC")
    fun loadSearchList(): Flow<List<SearchResultModel>?>

    @Query("SELECT * FROM search_result WHERE search_title = :title")
    suspend fun loadSearchByTitle(title: String): SearchResultModel?


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg search: SearchResultModel)

    @Update()
    fun update(vararg search: SearchResultModel)

    @Query("DELETE FROM search_result WHERE search_title = :title")
    fun delete(title: String)

    @Query("DELETE FROM search_result")
    fun deleteAll()

}