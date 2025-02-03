package com.example.gulbit.fragment

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gulbit.R
import com.example.gulbit.database.DBManager_diary

class SentenceFragment : Fragment(R.layout.fragment_sentence) {

    lateinit var dbManagerDiary: DBManager_diary
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var diarybtn: Button
    lateinit var diary1: EditText
    lateinit var diary2: EditText
    lateinit var diary3: EditText

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fragment에서 findViewById를 사용할 때는 view를 통해 호출
        diarybtn = view.findViewById(R.id.diarybtn)
        diary1 = view.findViewById(R.id.diary1)
        diary2 = view.findViewById(R.id.diary2)
        diary3 = view.findViewById(R.id.diary3)

        dbManagerDiary = DBManager_diary(requireContext(), "diaryDB", null, 1) // Context 전달

        diarybtn.setOnClickListener {
            val strDiary1 = diary1.text.toString()
            val strDiary2 = diary2.text.toString()
            val strDiary3 = diary3.text.toString()

            if (strDiary1.isNotEmpty() && strDiary2.isNotEmpty() && strDiary3.isNotEmpty()) {

                sqlitedb = dbManagerDiary.writableDatabase
                sqlitedb.execSQL("INSERT INTO diaryTBL (diary1, diary2, diary3) VALUES ('$strDiary1', '$strDiary2', '$strDiary3')")
                sqlitedb.close()

                Toast.makeText(requireContext(), "저장됨", Toast.LENGTH_SHORT).show() // Context 수정
            }
        }
    }
}
