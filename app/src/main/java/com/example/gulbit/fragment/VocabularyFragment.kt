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
 * Use the [VocabularyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class VocabularyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // inflate을 통해 Fragment클래스와 보여줄 xml레이아웃을 연결해주기
        val view = inflater.inflate(R.layout.fragment_vocabulary, container, false)

        // '학습하기' 버튼을 찾고 클릭 리스너 설정
        val btnLearn: Button = view.findViewById(R.id.btn_learn)
        btnLearn.setOnClickListener {
            // '학습하기' 버튼 클릭 시 WordCardFragment로 화면 전환
            findNavController().navigate(R.id.action_nav_learning_to_wordCardFragment)
        }

        return view
    }
}
