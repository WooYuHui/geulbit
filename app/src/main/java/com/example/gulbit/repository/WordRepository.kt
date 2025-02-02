package com.example.gulbit.repository

import com.example.gulbit.database.WordDatabaseHelper
import com.example.gulbit.model.Word

class WordRepository(private val dbHelper: WordDatabaseHelper) {

    // ✅ 단어 추가
    fun insertWord(word: Word) {
        val db = dbHelper.writableDatabase
        val query = """
            INSERT INTO ${WordDatabaseHelper.TABLE_WORDS} 
            (${WordDatabaseHelper.COLUMN_WORD}, ${WordDatabaseHelper.COLUMN_MEANING}, ${WordDatabaseHelper.COLUMN_EXAMPLE})
            VALUES ('${word.word}', '${word.meaning}', '${word.example}')
        """.trimIndent()
        db.execSQL(query)
        db.close()
    }

    // ✅ 모든 단어 가져오기
    fun getAllWords(): List<Word> {
        val words = mutableListOf<Word>()
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM ${WordDatabaseHelper.TABLE_WORDS}", null)

        while (cursor.moveToNext()) {
            val word = Word(
                cursor.getString(cursor.getColumnIndexOrThrow(WordDatabaseHelper.COLUMN_WORD)),
                cursor.getString(cursor.getColumnIndexOrThrow(WordDatabaseHelper.COLUMN_MEANING)),
                cursor.getString(cursor.getColumnIndexOrThrow(WordDatabaseHelper.COLUMN_EXAMPLE))
            )
            words.add(word)
        }
        cursor.close()
        db.close()
        return words
    }
}
