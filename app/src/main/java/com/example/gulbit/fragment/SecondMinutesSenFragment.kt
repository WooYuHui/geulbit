package com.example.gulbit.fragment

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.gulbit.R
import com.example.gulbit.database.DBManager_diary

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SecondMinutesSenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SecondMinutesSenFragment : Fragment(R.layout.fragment_second_minutes_sen) {
    lateinit var dbManagerDiary: DBManager_diary
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var diarybtn3: Button
    lateinit var diary1: EditText
    lateinit var diary2: EditText
    lateinit var diary3: EditText

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment에서 findViewById를 사용할 때는 view를 통해 호출
        diarybtn3 = view.findViewById(R.id.diarybtn3)
        diary1 = view.findViewById(R.id.diary1)
        diary2 = view.findViewById(R.id.diary2)
        diary3 = view.findViewById(R.id.diary3)

        dbManagerDiary = DBManager_diary(requireContext(), "diaryDB", null, 1) // Context 전달

        diarybtn3.setOnClickListener {
            val strDiary1 = diary1.text.toString()
            val strDiary2 = diary2.text.toString()
            val strDiary3 = diary3.text.toString()

            if (strDiary1.isNotEmpty() && strDiary2.isNotEmpty() && strDiary3.isNotEmpty()) {

                sqlitedb = dbManagerDiary.writableDatabase
                sqlitedb.execSQL("INSERT INTO diaryTBL (diary1, diary2, diary3) VALUES ('$strDiary1', '$strDiary2', '$strDiary3')")
                sqlitedb.close()

                Toast.makeText(requireContext(), "저장됨", Toast.LENGTH_SHORT).show() // Context 수정
            }
            findNavController().navigate(R.id.action_secondMinutesSenFragment_to_nav_timed_course)
        }
        val toolbar: Toolbar = view.findViewById(R.id.toolbar3)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
