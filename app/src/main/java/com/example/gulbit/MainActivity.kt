package com.example.gulbit

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var noButton: Button
    private lateinit var yesButton: Button
    private var newsList: List<News> = emptyList()
    private var currentNews: News? = null
    private lateinit var newsHelper: NewsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.contents)
        noButton = findViewById(R.id.dontKnow)
        yesButton = findViewById(R.id.didKnow)

        // NewsHelper 객체 생성
        newsHelper = NewsHelper(this)

        // 데이터베이스에서 뉴스 목록을 비동기로 가져옴
        CoroutineScope(Dispatchers.IO).launch {
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "news_database").build()
            newsList = db.newsDao().getAllNews()

            // UI 업데이트는 메인 스레드에서
            withContext(Dispatchers.Main) {
                // 오늘 날짜가 바뀌었으면 뉴스 변경
                if (newsHelper.isNewDay()) {
                    changeNews()
                } else {
                    // 이미 저장된 뉴스 표시
                    currentNews?.let {
                        textView.text = it.title
                    }
                }
            }
        }

        // "잘 이해 못했어요" 버튼 누를 시 설명 표시
        noButton.setOnClickListener {

        }

        // "이해했어요" 버튼 누를 시 다음 화면으로
        yesButton.setOnClickListener{

        }
    }

    private fun changeNews() {
        // 오늘 날짜에 해당하는 뉴스 항목을 찾기
        val todayDate = newsHelper.getTodayDate()

        // 오늘 날짜에 해당하는 뉴스 항목을 찾고, 다음 뉴스로 변경
        currentNews = newsList.find { it.date.toString() == todayDate }

        currentNews?.let {
            textView.text = it.title
        }

        // 오늘 날짜를 SharedPreferences에 저장
        newsHelper.saveTodayDate(todayDate)
    }
}