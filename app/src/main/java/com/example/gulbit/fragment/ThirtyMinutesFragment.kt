package com.example.gulbit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.gulbit.R
import com.example.gulbit.adapter.WordPagerAdapter
import com.example.gulbit.database.WordDatabaseHelper
import com.example.gulbit.databinding.FragmentThirtyMinutesBinding // ✅ 바인딩 클래스 수정
import com.example.gulbit.model.Word

class ThirtyMinutesFragment : Fragment(R.layout.fragment_thirty_minutes) {
    private lateinit var binding: FragmentThirtyMinutesBinding
    private lateinit var dbHelper: WordDatabaseHelper
    private lateinit var wordList: List<Word>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentThirtyMinutesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = binding.toolbar4
        val btnNext: Button = binding.btnNext4
        val viewPager: ViewPager2 = binding.viewPager2

        //  툴바 뒤로 가기 버튼 동작 설정
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        //  데이터베이스에서 단어 가져오기
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

        //  단어 목록에서 최대 9개만 가져오기
        wordList = wordList.take(9)

        //  ViewPager2에 어댑터 설정
        viewPager.adapter = WordPagerAdapter(wordList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //  "다음" 버튼 초기에는 숨기기
        btnNext.visibility = View.GONE

        //  마지막(9번째) 단어일 때 "다음" 버튼 보이게 설정
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                btnNext.visibility = if (position == wordList.size - 1) View.VISIBLE else View.GONE
            }
        })

        //  "다음" 버튼 클릭 시 화면 이동
        btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_thirtyMinutesFragment_to_thirtyMinutesLiFragment)
        }
    }
}
