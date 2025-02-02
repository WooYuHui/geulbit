package com.example.gulbit

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var dbManagerDiary: DBManagerDiary
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var diarybtn: Button
    lateinit var diary1: EditText
    lateinit var diary2: EditText
    lateinit var diary3: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        diarybtn = findViewById(R.id.diarybtn)
        diary1 = findViewById(R.id.diary1)
        diary2 = findViewById(R.id.diary2)
        diary3 = findViewById(R.id.diary3)

        dbManagerDiary = DBManagerDiary(this) // 수정: this -> context 전달

        diarybtn.setOnClickListener {
            val strDiary1 = diary1.text.toString()
            val strDiary2 = diary2.text.toString()
            val strDiary3 = diary3.text.toString()

            if (strDiary1.isNotEmpty() && strDiary2.isNotEmpty() && strDiary3.isNotEmpty()) {

                    sqlitedb = dbManagerDiary.writableDatabase
                    sqlitedb.execSQL("INSERT INTO diaryTBL (diary1, diary2, diary3) VALUES ('$strDiary1', '$strDiary2', '$strDiary3')")
                    sqlitedb.close()

                    Toast.makeText(applicationContext, "저장됨", Toast.LENGTH_SHORT).show()

            }
        }
    }

    inner class DBManagerDiary(context: Context) : SQLiteOpenHelper(context, "diaryDB", null, 1) {
        override fun onCreate(db: SQLiteDatabase?) {
            db?.execSQL("CREATE TABLE IF NOT EXISTS diaryTBL (diary1 TEXT PRIMARY KEY, diary2 TEXT, diary3 TEXT)")
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db?.execSQL("DROP TABLE IF EXISTS diaryTBL")
            onCreate(db)
        }
    }


}