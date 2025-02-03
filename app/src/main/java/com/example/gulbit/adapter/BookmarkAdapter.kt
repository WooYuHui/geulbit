package com.example.gulbit.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gulbit.R
import com.example.gulbit.model.Bookmark


class BookmarkAdapter(
    private val context: Context,
    private val bookmarks: List<Bookmark>
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    // ViewHolder 클래스: 각 아이템을 나타내는 뷰를 다룸
    inner class BookmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordTextView: TextView = itemView.findViewById(R.id.bookmark_word)
        val meaningTextView: TextView = itemView.findViewById(R.id.bookmark_meaning)
    }

    // RecyclerView의 아이템 개수 리턴
    override fun getItemCount(): Int {
        return bookmarks.size
    }

    // 각 아이템의 레이아웃을 바인딩
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.bookmark_item, parent, false)
        return BookmarkViewHolder(view)
    }

    // 아이템 데이터 설정
    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        val bookmark = bookmarks[position]
        holder.wordTextView.text = bookmark.word
        holder.meaningTextView.text = bookmark.meaning
    }
}