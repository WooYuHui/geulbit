package com.example.gulbit

import androidx.room.Entity
import androidx.room.PrimaryKey

// 뉴스 항목을 저장할 엔티티 클래스

@Entity(tableName = "news_table")
data class News(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val date: String // 날짜 설정
)
