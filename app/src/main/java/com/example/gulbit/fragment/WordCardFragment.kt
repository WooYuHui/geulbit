package com.example.gulbit.fragment;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
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

        // DB 초기화
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

        // ✅ View Binding 사용하여 viewPager 접근
        binding.viewPager.adapter = WordPagerAdapter(wordList)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
    }
}
