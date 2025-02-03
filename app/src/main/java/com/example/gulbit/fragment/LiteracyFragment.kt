package com.example.gulbit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gulbit.R
import com.example.gulbit.database.NewsDatabaseHelper
import com.example.gulbit.repository.NewsRepository

class LiteracyFragment : Fragment(R.layout.fragment_literacy) {
    private lateinit var textView: TextView
    private lateinit var noButton: Button
    private lateinit var yesButton: Button
    private lateinit var newsHelper: NewsRepository
    private lateinit var dbHelper: NewsDatabaseHelper
    private lateinit var addExplain: LinearLayout
    private lateinit var word1: TextView
    private lateinit var word2: TextView
    private lateinit var btn1: ImageButton
    private lateinit var btn2: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_literacy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar의 뒤로가기 버튼 설정
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        textView = view.findViewById(R.id.contents)
        noButton = view.findViewById(R.id.dontKnow)
        yesButton = view.findViewById(R.id.didKnow)
        addExplain = view.findViewById(R.id.addExplain)
        word1 = view.findViewById(R.id.word1)
        word2 = view.findViewById(R.id.word2)
        btn1 = view.findViewById(R.id.noteBtn1)
        btn2 = view.findViewById(R.id.noteBtn2)

        // SQLite 데이터베이스 헬퍼 초기화
        dbHelper = NewsDatabaseHelper(requireContext())
        newsHelper = NewsRepository(requireContext())
        dbHelper.clearAllBookmarks()

        // 오늘 날짜 확인 후 뉴스 변경
        if (newsHelper.isNewDay()) {
            changeNews()
        } else {
            showTodayNews()
        }

        // "잘 이해 못했어요" 버튼 클릭 시 설명 추가 표시
        noButton.setOnClickListener {
            addExplain.visibility = View.VISIBLE
        }

        yesButton.setOnClickListener {
            findNavController().navigate(R.id.action_literacyFragment_to_reviewNoteFragment)
        }


        // 첫 번째 북마크 버튼
        btn1.setOnClickListener {
            val word = word1.text.toString()
            val meaning = dbHelper.getMeaningForWord(word)

            if (!dbHelper.isBookmarkExist(word) && meaning != null) { // 중복 검증
                dbHelper.addBookmark(word, meaning)
                btn1.setImageResource(R.drawable.ee)
                Toast.makeText(requireContext(), "$word 단어가 북마크되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "$word 단어는 이미 북마크에 추가되어 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 두 번째 북마크 버튼
        btn2.setOnClickListener {
            val word = word2.text.toString()
            val meaning = dbHelper.getMeaningForWord(word)

            if (!dbHelper.isBookmarkExist(word) && meaning != null) { // 중복 검증
                dbHelper.addBookmark(word, meaning)
                btn2.setImageResource(R.drawable.ee)
                Toast.makeText(requireContext(), "$word 단어가 북마크되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "$word 단어는 이미 북마크에 추가되어 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showTodayNews() {
        val todayDate = newsHelper.getTodayDate()
        val (newsContent, newsId) = dbHelper.getNewsByDate(todayDate)

        // UI 업데이트
        textView.text = newsContent ?: "오늘의 뉴스가 없습니다."

        if (newsId > 0) {
            val newsWords = dbHelper.getWordsForNews(newsId)

            if (newsWords.isNotEmpty()) {
                word1.text = newsWords.getOrNull(0)?.first ?: "단어 없음"
                word2.text = newsWords.getOrNull(1)?.first ?: "단어 없음"
            }
        } else {
            word1.text = "단어 없음"
            word2.text = "단어 없음"
        }
    }

    private fun changeNews() {
        val todayDate = newsHelper.getTodayDate()
        val (newsContent, newsId) = dbHelper.getNewsByDate(todayDate)

        // UI 업데이트
        textView.text = newsContent ?: "오늘의 뉴스가 없습니다."

        if (newsId > 0) {
            val newsWords = dbHelper.getWordsForNews(newsId)

            if (newsWords.isNotEmpty()) {
                word1.text = newsWords.getOrNull(0)?.first ?: "단어 없음"
                word2.text = newsWords.getOrNull(1)?.first ?: "단어 없음"
            }
        } else {
            word1.text = "단어 없음"
            word2.text = "단어 없음"
        }

        // 오늘 날짜를 저장
        newsHelper.saveTodayDate(todayDate)
    }
}
