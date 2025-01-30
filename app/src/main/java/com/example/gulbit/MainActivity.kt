package com.example.gulbit
import android.os.Bundle
import android.util.Log
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
            val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "news_database").build()
            newsList = db.newsDao().getAllNews()

            // 데이터가 비어 있으면 JSON에서 불러와서 삽입
            if (newsList.isEmpty()) {
                loadAndInsertNewsFromJson(db)
            }

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

        // "잘 이해 못했어요" 버튼 누를 시 설명 표시
        noButton.setOnClickListener {

        }

        // "이해했어요" 버튼 누를 시 다음 화면으로
        yesButton.setOnClickListener {
            // 다음 화면으로 이동
        }
    }

    private fun changeNews() {
        // 오늘 날짜에 해당하는 뉴스 항목을 찾기
        val todayDate = newsHelper.getTodayDate()

        // 오늘 날짜에 해당하는 뉴스 항목을 찾고, 다음 뉴스로 변경
        currentNews = newsList.find { it.date == todayDate }

        currentNews?.let {
            textView.text = it.title
        }

        // 오늘 날짜를 SharedPreferences에 저장
        newsHelper.saveTodayDate(todayDate)
    }

    private suspend fun loadAndInsertNewsFromJson(db: AppDatabase) {
        // assets 폴더에서 JSON 파일 읽기
        val newsListFromJson = loadNewsFromAssets()

        // Room 데이터베이스에 뉴스 리스트 삽입
        val newsDao = db.newsDao()
        newsDao.insertAll(newsListFromJson)

        // 데이터베이스에 저장된 뉴스 목록 가져오기
        newsList = newsDao.getAllNews()
    }

    private fun loadNewsFromAssets(): List<News> {
        val jsonString = assets.open("news_data.json").use {
            InputStreamReader(it).readText()
        }

        // Gson을 사용해 JSON을 List<News>로 변환
        val newsType = object : TypeToken<List<News>>() {}.type
        return Gson().fromJson(jsonString, newsType)
    }
}