package com.example.gulbit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gulbit.R
import com.example.gulbit.adapter.BookmarkAdapter
import com.example.gulbit.database.NewsDatabaseHelper
import com.example.gulbit.model.Bookmark

class ReviewNoteFragment : Fragment(R.layout.fragment_review_note) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var bookmarkAdapter: BookmarkAdapter
    private lateinit var dbHelper: NewsDatabaseHelper
    private lateinit var bookmarks: List<Bookmark>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar의 뒤로가기 버튼 설정 (액티비티에서 관리)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // DB에서 북마크된 단어들 가져오기
        dbHelper = NewsDatabaseHelper(requireContext())
        bookmarks = dbHelper.getAllBookmarks()

        // 어댑터 연결
        bookmarkAdapter = BookmarkAdapter(requireContext(), bookmarks)
        recyclerView.adapter = bookmarkAdapter

        //  finishBtn을 view에서 찾아오기
        val finishBtn: Button = view.findViewById(R.id.finishBtn)
        finishBtn.setOnClickListener {
            findNavController().navigate(R.id.action_reviewNoteFragment_to_nav_learning)
        }
    }
}
