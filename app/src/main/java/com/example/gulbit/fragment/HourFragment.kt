package com.example.gulbit.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.gulbit.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HourFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HourFragment : Fragment(R.layout.fragment_hour) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnFourWords: Button = view.findViewById(R.id.course1btn)
        val btnSecondWords: Button = view.findViewById(R.id.course2btn)

        // course1btn 클릭 시 navigateToWordCard() 호출
        btnFourWords.setOnClickListener { navigateToWordCard(4) }

        // course2btn 클릭 시 navigateToWordCard() 호출
        btnSecondWords.setOnClickListener { navigateToWordCard(7) }
    }

    // navigateToWordCard()를 수정하여 인자에 맞는 숫자 설정
    private fun navigateToWordCard(wordCount: Int) {
        val bundle = Bundle().apply {
            putInt("wordCount", wordCount) // 클릭한 버튼에 따라 단어 개수 설정
        }

        // 조건에 맞는 Fragment로 이동
        if (wordCount == 4) {
            findNavController().navigate(R.id.action_nav_timed_course_to_tenMinutesFragment)
        } else if (wordCount == 7) {
            findNavController().navigate(R.id.action_nav_timed_course_to_secondMinutesFragment)
        }
    }
}

