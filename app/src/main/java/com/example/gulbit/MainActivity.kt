package com.example.gulbit
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
            val db = withContext(Dispatchers.IO) {
                Room.databaseBuilder(applicationContext, AppDatabase::class.java, "news_database").build()
            }
            newsList = withContext(Dispatchers.IO) { db.newsDao().getAllNews() }

            if (newsList.isEmpty()) {
                loadAndInsertNewsFromJson(db)
                newsList = withContext(Dispatchers.IO) { db.newsDao().getAllNews() }
            }

            if (newsHelper.isNewDay()) {
                changeNews()
            } else {
                currentNews?.let {
                    textView.text = it.title
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
        }
    }

    private fun changeNews() {
        val todayDate = newsHelper.getTodayDate()
        currentNews = newsList.find { it.date == todayDate }

        if (currentNews != null) {
            textView.text = currentNews!!.title
        } else {
            textView.text = "오늘의 뉴스가 없습니다." // 예외 처리
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