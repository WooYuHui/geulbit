package com.example.gulbit
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.gulbit.database.NewsDatabaseHelper

class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var noButton: Button
    private lateinit var yesButton: Button
    private lateinit var newsHelper: NewsHelper
    private lateinit var dbHelper: NewsDatabaseHelper
    private lateinit var addExplain: LinearLayout
    private lateinit var word1: TextView
    private lateinit var word2: TextView
    private lateinit var btn1: Button
    private lateinit var btn2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.contents)
        noButton = findViewById(R.id.dontKnow)
        yesButton = findViewById(R.id.didKnow)
        addExplain = findViewById(R.id.addExplain)
        word1 = findViewById(R.id.word1)
        word2= findViewById(R.id.word2)
        btn1 = findViewById(R.id.noteBtn1)
        btn2 = findViewById(R.id.noteBtn2)

        // SQLite 데이터베이스 헬퍼 초기화
        dbHelper = NewsDatabaseHelper(this)
        newsHelper = NewsHelper(this)

        // 오늘 날짜 확인 후 뉴스 변경
        if (newsHelper.isNewDay()) {
            changeNews()
        } else {
            showTodayNews()
        }

        // "잘 이해 못했어요" 버튼 클릭 시 설명 추가 표시
        noButton.setOnClickListener {
            addExplain.visibility = View.VISIBLE
        }

        // "이해했어요" 버튼 클릭 시 다음 화면으로 이동
        yesButton.setOnClickListener {
            val intent = Intent(this, ReviewNote::class.java)
            startActivity(intent)
        }

        // 첫번째 북마크 버튼
        btn1.setOnClickListener{
            val word = word1.text.toString()
            val meaning = dbHelper.getMeaningForWord(word)

            if (meaning != null) {
                // 북마크 추가
                dbHelper.addBookmark(word, meaning)

                Toast.makeText(this, "$word 단어가 북마크되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "$word 의 의미를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 두번째 북마크 버튼
        btn2.setOnClickListener{
            val word = word2.text.toString()
            val meaning = dbHelper.getMeaningForWord(word)

            if (meaning != null) {
                // 북마크 추가
                dbHelper.addBookmark(word, meaning)

                Toast.makeText(this, "$word 단어가 북마크되었습니다.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "$word 의 의미를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun showTodayNews() {
        val todayDate = newsHelper.getTodayDate()
        val (newsContent, newsId) = dbHelper.getNewsByDate(todayDate)

        // UI 스레드에서 처리
        runOnUiThread {
            textView.text = newsContent ?: "오늘의 뉴스가 없습니다."

            // 뉴스 ID가 0 이상일 경우, 단어들 가져오기
            if (newsId > 0) {
                val newsWords = dbHelper.getWordsForNews(newsId)

                // 단어가 있으면 word1, word2에 설정
                if (newsWords.isNotEmpty()) {
                    word1.text = newsWords.getOrNull(0)?.first ?: "단어 없음"
                    word2.text = newsWords.getOrNull(1)?.first ?: "단어 없음"
                }
            } else {
                // 뉴스 ID가 잘못되었을 경우 "단어 없음" 표시
                word1.text = "단어 없음"
                word2.text = "단어 없음"
            }
        }
    }

    private fun changeNews() {
        val todayDate = newsHelper.getTodayDate()

        val (newsContent, newsId) = dbHelper.getNewsByDate(todayDate)

        // 뉴스 콘텐츠 띄우기
        runOnUiThread {
            textView.text = newsContent ?: "오늘의 뉴스가 없습니다."

            // 뉴스 ID가 0 이상일 경우, 관련 단어들을 가져오기
            if (newsId > 0) {
                val newsWords = dbHelper.getWordsForNews(newsId)

                // 단어들이 있으면 word1, word2에 설정
                if (newsWords.isNotEmpty()) {
                    word1.text = newsWords.getOrNull(0)?.first ?: "단어 없음"
                    word2.text = newsWords.getOrNull(1)?.first ?: "단어 없음"
                }
            } else {
                // 뉴스 ID가 0인 경우 "단어 없음" 표시
                word1.text = "단어 없음"
                word2.text = "단어 없음"
            }
        }

        // 오늘 날짜를 저장
        newsHelper.saveTodayDate(todayDate)
    }
}