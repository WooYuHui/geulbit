package com.example.gulbit

import android.app.Application
import androidx.room.Room

class MyApplication : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "news_database").build()
    }
}