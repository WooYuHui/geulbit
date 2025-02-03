package com.example.gulbit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.gulbit.R
import com.example.gulbit.adapter.WordPagerAdapter
import com.example.gulbit.database.WordDatabaseHelper
import com.example.gulbit.databinding.FragmentSecondMinutesBinding
import com.example.gulbit.databinding.FragmentTenMinutesBinding
import com.example.gulbit.model.Word

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondMinutesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondMinutesFragment : Fragment(R.layout.fragment_second_minutes) {
    private lateinit var binding: FragmentSecondMinutesBinding // 수정된 바인딩 클래스
    private lateinit var dbHelper: WordDatabaseHelper
    private lateinit var wordList: List<Word>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 바인딩 클래스 초기화
        binding = FragmentSecondMinutesBinding.inflate(inflater, container, false) // 수정된 바인딩 클래스
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = binding.toolbar3
        val btnNext: Button = binding.btnNext2
        val viewPager: ViewPager2 = binding.viewPager2

        // 데이터베이스에서 단어 가져오기
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

        // 7개만 가져와서 ViewPager에 설정
        wordList = wordList.take(7)

        viewPager.adapter = WordPagerAdapter(wordList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // "다음" 버튼 초기에는 숨기기
        btnNext.visibility = View.GONE

        // 마지막(7번째) 단어일 때 버튼 보이게 하기
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                btnNext.visibility = if (position == wordList.size - 1) View.VISIBLE else View.GONE
            }
        })

        // "다음" 버튼 클릭 시 화면 이동
        btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_secondMinutesFragment_to_secondMinutesSenFragment)
        }

        // 툴바 설정
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar3) // 수정된 바인딩 사용
        binding.toolbar3.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}