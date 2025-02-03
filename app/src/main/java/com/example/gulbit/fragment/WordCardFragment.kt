package com.example.gulbit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.navigation.fragment.findNavController
import com.example.gulbit.R
import com.example.gulbit.adapter.WordPagerAdapter
import com.example.gulbit.database.WordDatabaseHelper
import com.example.gulbit.databinding.FragmentWordCardBinding
import com.example.gulbit.model.Word

class WordCardFragment : Fragment(R.layout.fragment_word_card) {

    private lateinit var binding: FragmentWordCardBinding
    private lateinit var dbHelper: WordDatabaseHelper
    private lateinit var wordList: List<Word>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val btnNext: Button = view.findViewById(R.id.btn_next)

        // ✅ `HourFragment`에서 전달받은 단어 개수 (기본값: 20)
        val wordCount = arguments?.getInt("wordCount", 20) ?: 20

        // ✅ DB에서 단어 리스트 가져오기
        dbHelper = WordDatabaseHelper(requireContext())
        wordList = dbHelper.readableDatabase.let { dbHelper ->
            val words = mutableListOf<Word>()
            val cursor = dbHelper.rawQuery("SELECT * FROM ${WordDatabaseHelper.TABLE_WORDS}", null)
            while (cursor.moveToNext()) {
                words.add(
                    Word(
                        cursor.getString(cursor.getColumnIndexOrThrow(WordDatabaseHelper.COLUMN_WORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(WordDatabaseHelper.COLUMN_MEANING)),
                        cursor.getString(cursor.getColumnIndexOrThrow(WordDatabaseHelper.COLUMN_EXAMPLE))
                    )
                )
            }
            cursor.close()
            dbHelper.close()
            words
        }

        // ✅ 단어 개수를 4개로 제한
        wordList = wordList.take(4)

        // ✅ ViewPager 설정
        val viewPager = binding.viewPager
        viewPager.adapter = WordPagerAdapter(wordList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // ✅ 처음에는 버튼 숨기기
        btnNext.visibility = View.GONE

        // ✅ 단어가 4개 모드일 때만, 마지막(4번째) 단어에서 "다음" 버튼 보이게 설정
        if (wordCount == 4) {
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    btnNext.visibility = if (position == wordList.size - 1) View.VISIBLE else View.GONE
                }
            })
        }

        // ✅ 버튼 클릭 시 `SentenceFragment`로 이동
        btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_wordCardFragment_to_nav_sentence)
        }

        // ✅ 툴바 설정
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}


