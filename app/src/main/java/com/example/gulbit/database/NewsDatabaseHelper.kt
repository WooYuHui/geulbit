package com.example.gulbit.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class NewsDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createNewsTable = "CREATE TABLE $TABLE_NEWS (" +
                "$COLUMN_NEWS_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_DATE TEXT NOT NULL, " +
                "$COLUMN_CONTENT TEXT NOT NULL)"
        db.execSQL(createNewsTable)

        val createWordTable = """
            CREATE TABLE $TABLE_WORDS (
                $COLUMN_WORD_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NEWS_ID INTEGER NOT NULL,
                $COLUMN_WORD TEXT NOT NULL,
                $COLUMN_MEANING TEXT NOT NULL,
                FOREIGN KEY($COLUMN_NEWS_ID) REFERENCES $TABLE_NEWS($COLUMN_NEWS_ID) ON DELETE CASCADE
            );
        """.trimIndent()
        db.execSQL(createWordTable)

        val createBookmarkTable = """
            CREATE TABLE IF NOT EXISTS $TABLE_BOOKMARKS (
                $COLUMN_WORD TEXT NOT NULL,
                $COLUMN_MEANING TEXT NOT NULL,
                $COLUMN_DATE TEXT
                            );
        """.trimIndent()


        // 기본 뉴스 데이터 삽입
        val defaultNews = listOf(
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-03', '일상에서 늘 만나던 사람들, 늘 보던 정보 매체가 주는 익숙함과 편안함에 길들여져 살면 어느새 나도 모르게 한쪽으로 굳어진 시각과 관점을 갖게 된다. \n" +
                    "정치를 보는 눈도 마찬가지다. 확증편향은 건강한 인간관계를 무너뜨리고 사회 통합을 가로막으며, 심지어는 생명을 죽이는 끔찍한 결과를 가져오기도 한다.\n" +
                    "나치의 홀로코스트는 확증편향이 불러온 끔찍한 역사적 비극이다. 나치는 홀로코스트를 정당화하고 유대인을 탄압하기 위해 유대인을 악마화하는 선전을 펼쳤다. \n" +
                    "유대인을 경제적 이익만 취하는 기생충으로 묘사하고, 국가 전복을 꿈꾸는 배후자로 선전하는가 하면 열등한 인종으로 규정했다. 유대인들의 사회 기여도와 인간성을 보여주는 증거들은 나치의 통제 아래 철저히 무시했다. 나치의 거짓 뉴스와 선동은 반복적으로 유포됐다. 독일 국민은 유대인이 열등하고 저열한 인종이라는 확증편향을 갖게 되었고 대학살 정책에 동조하거나 침묵했다.')",
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-04', '한강은 강렬한 주제 의식, 통념을 깨는 이미지들, 파닥이는 언어들, 돌출하는 판타지로 직조한 소설 <채식주의자>로 먼저 세계 문단에 이름을 알렸다. 질곡의 역사와 야만적인 폭력에 눌린 여성의 저항을 담은 이 작품이 2016년 영국의 맨부커 인터내셔널상을 거머쥔 게 계기였다. 여주인공이 육식에 반응하는 구토와 자해 같은 행위는 생명을 수탈하는 무자비함에 대한 거부를 함의한다.\n" +
                    "\n" +
                    "정치사회적 맥락에서 폭력은 남성, 가부장제, 권력의 주류에서 나오고, 희생은 상대적으로 나약한 여성, 소수자, 비권력자의 몫이다. 작가는 희생자의 몸에 각인되는 폭력이 올바름을 상실한 권력과 권력의 속성인 무자비함에서 비롯되었음을 폭로한다. 육식에 항거하다가 나무로 바뀌는 기괴하고 도발적인 상상력은 가부장제의 반생명적 억압에 대한 식물적 분노를 담아낸 것이다.')",

            )
        defaultNews.forEach { db.execSQL(it) }

        val defaultWords = listOf(
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (1, '확증편향', '자신의 신념이나 가치관과 일치하는 정보만을 찾고, 반대되는 정보는 무시하는 사고방식')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (1, '선전하다 (宣傳)', '주의나 주장, 사물의 존재, 효능 따위를 많은 사람이 알고 이해하도록 잘 설명하여 널리 알리다.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (2, '직조하다 (織造)', '기계나 베틀 따위로 피륙을 짜다.')"
        )
        defaultWords.forEach{db.execSQL(it)}


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NEWS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORDS")
        onCreate(db)
    }

    fun getNewsByDate(date: String): Pair<String?, Int> {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_CONTENT, $COLUMN_NEWS_ID FROM $TABLE_NEWS WHERE $COLUMN_DATE = ?", arrayOf(date))

        var newsContent: String? = null
        var newsId = 0 // 기본값 0

        val contentIndex = cursor.getColumnIndex(COLUMN_CONTENT)
        val newsIdIndex = cursor.getColumnIndex(COLUMN_NEWS_ID)

        if (contentIndex != -1 && newsIdIndex != -1) {
            if (cursor.moveToFirst()) {
                newsContent = cursor.getString(contentIndex)
                newsId = cursor.getInt(newsIdIndex)
            }
        } else {
            Log.e("DB Error", "Columns not found: $COLUMN_CONTENT, $COLUMN_NEWS_ID")
        }

        cursor.close()

        return Pair(newsContent, newsId)
    }

    // 뉴스에 따른 키워드 가져오는 메서드
    fun getWordsForNews(newsId: Int): List<Pair<String, String>> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_WORDS,
            arrayOf(COLUMN_WORD, COLUMN_MEANING),
            "$COLUMN_NEWS_ID = ?",
            arrayOf(newsId.toString()),
            null, null, null
        )

        val wordList = mutableListOf<Pair<String, String>>()
        while (cursor.moveToNext()) {
            val word = cursor.getString(0)
            val meaning = cursor.getString(1)
            wordList.add(Pair(word, meaning))
        }
        cursor.close()
        db.close()
        return wordList
    }

    fun getMeaningForWord(word: String): String? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_WORDS,
            arrayOf(COLUMN_MEANING),
            "$COLUMN_WORD = ?",
            arrayOf(word),
            null, null, null
        )

        var meaning: String? = null
        if (cursor.moveToFirst()) {
            meaning = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING))
        }
        cursor.close()
        db.close()
        return meaning
    }

    fun addBookmark(word: String, meaning: String, date: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_WORD, word)
        contentValues.put(COLUMN_MEANING, meaning)
        contentValues.put(COLUMN_DATE, date)

        db.insert(TABLE_BOOKMARKS, null, contentValues)
        db.close()
    }
    companion object {
        private const val DATABASE_NAME = "news_database.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NEWS = "news"
        private const val COLUMN_NEWS_ID = "news_id"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_CONTENT = "content"

        private const val TABLE_WORDS = "words"
        private const val COLUMN_WORD_ID = "word_id"
        private const val COLUMN_WORD = "word"
        private const val COLUMN_MEANING = "meaning"

        private const val TABLE_BOOKMARKS = "bookmarks"
    }
}