package com.example.gulbit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.gulbit.R
import com.example.gulbit.adapter.WordPagerAdapter
import com.example.gulbit.database.WordDatabaseHelper
import com.example.gulbit.databinding.FragmentWordCardBinding
import com.example.gulbit.model.Word

class WordCardFragment : Fragment(R.layout.fragment_word_card) {

    private var _binding: FragmentWordCardBinding? = null
    private val binding get() = _binding!!
    private lateinit var dbHelper: WordDatabaseHelper
    private lateinit var wordList: List<Word>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWordCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)

        // 툴바 설정
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = "어휘력 학습"
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        // DB에서 단어 리스트 가져오기
        dbHelper = WordDatabaseHelper(requireContext())
        val words = mutableListOf<Word>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${WordDatabaseHelper.TABLE_WORDS}", null)

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
        db.close()
        wordList = words

        // ViewPager2 설정
        binding.viewPager.adapter = WordPagerAdapter(wordList)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // "학습 완료" 버튼 숨김 상태에서 시작
        binding.btnComplete.visibility = View.GONE

        // 마지막 단어 감지하여 버튼 표시
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnComplete.visibility = if (position == wordList.size - 1) View.VISIBLE else View.GONE
            }
        })

        // "학습 완료" 버튼 클릭 이벤트
        binding.btnComplete.setOnClickListener {
            Toast.makeText(requireContext(), "학습을 완료했습니다! 🎉", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}