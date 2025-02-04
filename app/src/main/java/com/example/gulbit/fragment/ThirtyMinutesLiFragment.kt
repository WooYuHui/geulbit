package com.example.gulbit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gulbit.R
import com.example.gulbit.databinding.FragmentThirtyMinutesLiBinding

class ThirtyMinutesLiFragment : Fragment(R.layout.fragment_thirty_minutes_li) {
    private var _binding: FragmentThirtyMinutesLiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThirtyMinutesLiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupButtons()
        initializeExplanationVisibility()
    }

    private fun setupToolbar() {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar4)
        binding.toolbar4.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupButtons() {
        // "잘 이해하지 못했어요" 버튼 클릭 시 설명 표시
        binding.dontKnow.setOnClickListener {
            showExplanation()
        }

        // "이해했어요" 버튼 클릭 시 다른 Fragment로 이동
        binding.didKnow.setOnClickListener {
            findNavController().navigate(R.id.action_thirtyMinutesLiFragment_to_thirtyMinutesSenFragment)
        }
    }

    private fun initializeExplanationVisibility() {
        //  초기에는 설명 부분을 숨김
        binding.explanationContainer.visibility = View.GONE
    }

    private fun showExplanation() {
        //  설명 부분을 보이게 설정
        binding.explanationContainer.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
