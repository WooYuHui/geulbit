package com.example.gulbit.fragment

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gulbit.R

class HourFragment : Fragment(R.layout.fragment_hour) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnFourWords: Button = view.findViewById(R.id.course1btn)
        val btnSevenWords: Button = view.findViewById(R.id.course2btn)

        // 버튼 클릭 시 `navigateToWordCard()`를 호출하여 단어 개수 전달
        btnFourWords.setOnClickListener { navigateToWordCard(4) }
        btnSevenWords.setOnClickListener { navigateToWordCard(7) }
    }

    private fun navigateToWordCard(wordCount: Int) {
        val bundle = Bundle().apply {
            putInt("wordCount", wordCount) // 선택한 버튼에 따라 4 또는 7 전달
        }
        findNavController().navigate(R.id.action_hourFragment_to_wordCardFragment, bundle)
    }
}

