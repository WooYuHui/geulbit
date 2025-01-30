package com.example.gulbit

import android.content.Context
import android.content.SharedPreferences
import java.util.*

//매일 뉴스 항목을 바꾸기 위해 오늘 날짜를 SharedPreferences에 저장하고,
// 다음날 되면 새로운 뉴스 항목을 표시

class NewsHelper(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("NewsPrefs", Context.MODE_PRIVATE)

    // 오늘 날짜 가져오기 (yyyyMMdd 형식)
    fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1 // 0부터 시작하므로 1을 더함
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return "$year$month$day"
    }

    // 오늘 날짜 저장하기
    fun saveTodayDate(date: String) {
        sharedPreferences.edit().putString("today_date", date).apply()
    }

    // 저장된 날짜 가져오기
    fun getSavedDate(): String? {
        return sharedPreferences.getString("today_date", null)
    }

    // 날짜 비교: 오늘 날짜가 변경되었는지 확인
    fun isNewDay(): Boolean {
        val savedDate = getSavedDate()
        val todayDate = getTodayDate()

        return savedDate != todayDate
    }
}