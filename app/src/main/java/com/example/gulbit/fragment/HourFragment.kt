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

        // ✅ `navigateToWordCard()` 호출할 때 인자 없이 사용
        btnFourWords.setOnClickListener { navigateToWordCard() }
    }

    // ✅ 인자 없이 4를 직접 사용
    private fun navigateToWordCard() {
        val bundle = Bundle().apply {
            putInt("wordCount", 4) // ✅ 항상 4로 설정
        }
        findNavController().navigate(R.id.action_hourFragment_to_wordCardFragment, bundle)
    }
}

