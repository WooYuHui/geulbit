package com.example.gulbit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

// 데이터 삽입 및 조회 메서드
@Dao
interface NewsDAO {
    @Insert
    suspend fun insertAll(newsList: List<News>)

    @Query("SELECT * FROM news_table")
    suspend fun getAllNews(): List<News>

    @Query("SELECT * FROM news_table WHERE id = :id LIMIT 1")
    suspend fun getNewsById(id: Int): News
}