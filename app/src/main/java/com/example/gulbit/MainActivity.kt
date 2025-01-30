package com.example.gulbit
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var noButton: Button
    private lateinit var yesButton: Button
    private var newsList: List<News> = emptyList()
    private var currentNews: News? = null
    private lateinit var newsHelper: NewsHelper
    private lateinit var addExplain: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.contents)
        noButton = findViewById(R.id.dontKnow)
        yesButton = findViewById(R.id.didKnow)
        addExplain = findViewById(R.id.addExplain)

        // NewsHelper 객체 생성
        newsHelper = NewsHelper(this)

        // 데이터베이스에서 뉴스 목록을 비동기로 가져옴
        lifecycleScope.launch {
            // Room 데이터베이스 객체 가져오기
            val db = withContext(Dispatchers.IO) {
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "news_database").build()
            }

            // 데이터베이스에서 뉴스 목록 가져오기
            newsList = withContext(Dispatchers.IO) { db.newsDao().getAllNews() }

            // 뉴스 목록이 비어 있으면 JSON 파일에서 뉴스 로드하여 삽입
            if (newsList.isEmpty()) {
                loadAndInsertNewsFromJson(db) // 뉴스리스트를 넣었음
                newsList = withContext(Dispatchers.IO) { db.newsDao().getAllNews() }
            }

            // 오늘 날짜인지 확인하고 뉴스 변경
            if (newsHelper.isNewDay()) {
                changeNews()  // 새로운 날이라면 뉴스 변경
            } else {
                // 오늘 날짜와 일치하는 뉴스 찾기
                val todayDate = newsHelper.getTodayDate()
                currentNews = newsList.find { it.date == todayDate }

                Log.d("MainActivity", "오늘 날짜의 뉴스 가져오기: $currentNews")

                // 찾은 뉴스가 있으면 내용 표시, 없으면 기본 메시지
                if (currentNews != null) {
                    textView.text = currentNews!!.content
                } else {
                    textView.text = "오늘의 뉴스가 없습니다."
                }
            }
        }

        // "잘 이해 못했어요" 버튼 누를 시 설명 표시
        noButton.setOnClickListener {
            addExplain.visibility = View.VISIBLE
        }

        // "이해했어요" 버튼 누를 시 다음 화면으로
        yesButton.setOnClickListener {
            // 다음 화면으로 이동
            val intent = Intent(this, ReviewNote::class.java)
            startActivity(intent)
        }
    }

    private fun changeNews() {
        val todayDate = newsHelper.getTodayDate()
        currentNews = newsList.find { it.date == todayDate }

        // UI 업데이트는 반드시 메인 스레드에서 해야 함
        Log.d("MainActivity", "오늘의 날짜: $todayDate")
        Log.d("MainActivity", "현재 뉴스: $currentNews")

        runOnUiThread {
            if (currentNews != null) {
                textView.text = currentNews!!.content  // content를 출력
            } else {
                textView.text = "오늘의 뉴스가 없습니다." // 예외 처리
            }
        }

        newsHelper.saveTodayDate(todayDate)
    }

    private suspend fun loadAndInsertNewsFromJson(db: AppDatabase) {
        val newsListFromJson = loadNewsFromAssets()
        val newsDao = db.newsDao()

        withContext(Dispatchers.IO) {
            newsDao.insertAll(newsListFromJson)
        }

        // 삽입 후 데이터 다시 가져오기
        newsList = newsDao.getAllNews()
    }

    private fun loadNewsFromAssets(): List<News> {
        return try {
            val jsonString = assets.open("news_data.json").use {
                InputStreamReader(it).readText()
            }
            val newsType = object : TypeToken<List<News>>() {}.type
            Gson().fromJson(jsonString, newsType) ?: emptyList()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error loading news from JSON", e)
            emptyList()
        }
    }
}