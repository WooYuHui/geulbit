package com.example.gulbit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gulbit.database.Bookmark
import com.example.gulbit.database.NewsDatabaseHelper

class ReviewNote : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var dbHelper: NewsDatabaseHelper
    private lateinit var bookmarks: List<Bookmark>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reviewnote)

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // DB에서 북마크된 단어들 가져오기
        dbHelper = NewsDatabaseHelper(this)
        bookmarks = dbHelper.getAllBookmarks()

        // 어댑터 연결
        bookmarkAdapter = BookmarkAdapter(this, bookmarks)
        recyclerView.adapter = bookmarkAdapter
    }

}