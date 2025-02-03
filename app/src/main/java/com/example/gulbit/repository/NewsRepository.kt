package com.example.gulbit.repository

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

class NewsRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("NewsPrefs", Context.MODE_PRIVATE)

    // 오늘 날짜 가져오기 (yyyyMMdd 형식)
    fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(calendar.time)
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