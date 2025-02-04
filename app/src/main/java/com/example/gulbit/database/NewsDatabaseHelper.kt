package com.example.gulbit.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.gulbit.model.Bookmark

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
                $COLUMN_MEANING TEXT NOT NULL
                            );
        """.trimIndent()
        db.execSQL(createBookmarkTable)


        // 기본 뉴스 데이터 삽입
        val defaultNews = listOf(
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-04', '일상에서 늘 만나던 사람들, 늘 보던 정보 매체가 주는 익숙함과 편안함에 길들여져 살면 어느새 나도 모르게 한쪽으로 굳어진 시각과 관점을 갖게 된다. \n" +
                    "정치를 보는 눈도 마찬가지다. 확증편향은 건강한 인간관계를 무너뜨리고 사회 통합을 가로막으며, 심지어는 생명을 죽이는 끔찍한 결과를 가져오기도 한다.\n" +
                    "나치의 홀로코스트는 확증편향이 불러온 끔찍한 역사적 비극이다. 나치는 홀로코스트를 정당화하고 유대인을 탄압하기 위해 유대인을 악마화하는 선전을 펼쳤다. \n" +
                    "유대인을 경제적 이익만 취하는 기생충으로 묘사하고, 국가 전복을 꿈꾸는 배후자로 선전하는가 하면 열등한 인종으로 규정했다. 유대인들의 사회 기여도와 인간성을 보여주는 증거들은 나치의 통제 아래 철저히 무시했다. 나치의 거짓 뉴스와 선동은 반복적으로 유포됐다. 독일 국민은 유대인이 열등하고 저열한 인종이라는 확증편향을 갖게 되었고 대학살 정책에 동조하거나 침묵했다.')",
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-05', '한강은 강렬한 주제 의식, 통념을 깨는 이미지들, 파닥이는 언어들, 돌출하는 판타지로 직조한 소설 <채식주의자>로 먼저 세계 문단에 이름을 알렸다. 질곡의 역사와 야만적인 폭력에 눌린 여성의 저항을 담은 이 작품이 2016년 영국의 맨부커 인터내셔널상을 거머쥔 게 계기였다. 여주인공이 육식에 반응하는 구토와 자해 같은 행위는 생명을 수탈하는 무자비함에 대한 거부를 함의한다.\n" +
                    "\n" +
                    "정치사회적 맥락에서 폭력은 남성, 가부장제, 권력의 주류에서 나오고, 희생은 상대적으로 나약한 여성, 소수자, 비권력자의 몫이다. 작가는 희생자의 몸에 각인되는 폭력이 올바름을 상실한 권력과 권력의 속성인 무자비함에서 비롯되었음을 폭로한다. 육식에 항거하다가 나무로 바뀌는 기괴하고 도발적인 상상력은 가부장제의 반생명적 억압에 대한 식물적 분노를 담아낸 것이다.')",
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-06', '자존감과 반대되는 수치심은 인생의 목적을 이루려고 할 때 필요한 것이 결핍되면서 생겨나는 ‘좌절감’의 다른 이름이기도 하다. 롤스는 수치심을 ‘능력의 탁월성’의 부재 및 상실에 따른 자연적 수치심과 ‘인격의 탁월성’의 부재 및 상실에서 기인하는 도덕적 수치심으로 구분한다. \n" +
                    "롤스는 이 같은 자존감의 사회적 토대가 가장 중요한 사회적 재화이며, 정치적 평등의 원칙에 따라 이 같은 재화가 분배될 필요가 있다고 강조한다. 이때 필요한 것은 절차적 정의다. 자연적으로 타고난 능력이나 소질, 사회적 우연성에 의해 가장 불리한 사람을 최우선으로 고려하는 ‘최소극대화(Minimax)’ 원칙이 지켜져야 한다는 것이다.')",
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-07', '알뜰폰 인기도 급하강했다. 가입자 증가세가 꺾였다. 한국통신사업자연합회(KTOA)가 13일 발표한 지난해 알뜰폰 순증은 37만 7400만 여명이다. 이는 이동통신 3사에서 알뜰폰으로 갈아탄 인원에서 알뜰폰에서 통신 3사로 회귀한 이용자를 뺀 수치다. 2023년의 경우 알뜰폰 순증 가입자는 80만 896명이었으니, 순증 인원 감소폭은 무려 53%에 이른다. 이러다가는 알뜰폰 가입자 수가 1000만 명을 넘기기는커녕 900만 명을 지키기도 어렵게 됐다.\n" +
                    "2025년 역시 알뜰폰 업계로서는 좋을 일이 거의 없다. 정부의 지원은 축소되고 통신 3사의 저가요금제 공세는 더욱 강화된다. 더욱이 오는 6월부터는 국회에서 폐지된 이동통신 단말장치 유통구조 개선에 관한 법률(단통법)이 시행된다. 통신사로부터의 보조금 살표가 과거와는 다를 것이라는 전망에도 불구하고 지원금이 늘어날 것임은 자명하다.')",
            "INSERT INTO $TABLE_NEWS ($COLUMN_DATE, $COLUMN_CONTENT) VALUES ('2025-02-08', '‘전연지대의 토끼’는 설산에서 길을 잃은 남한의 남성 군인이 탈북자 여성을 만나는 이야기다. 생생한 묘사와 소설적 재미가 돋보인다는 점에서 몇몇 심사위원들의 강력한 지지가 있었으나 반대도 팽팽해 토론이 길고 지난했다. 남북한 관계를 다루면서도 역사 인식이 단순하고 추상적이며 이를 구원자 여성과의 성관계에 비유하는 낯익은 도식의 반복이 새롭지 않아 아쉬움이 남는다.\n" +
                    "‘미분처럼’은 어느 바리스타의 행사에서 헤어진 연인이 우연히 재회하는 하루를 그리고 있는 소설이다. 과거 연인에 대한 복잡한 심정이 커피 원두를 추출하는 섬세한 과정과 커피의 쓰고 떫은 맛에 대한 비유로 이어지는 흐름이 미려하고 정갈했다. 언뜻 사소한 마음을 그리고 있는 듯하지만 그 안에서 인간 본연의 결핍과 이기심, 인간관계의 위태로운 불균형을 통찰하는 진득한 시선이 미더웠다.')"
        )
        defaultNews.forEach { db.execSQL(it) }

        val defaultWords = listOf(
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (1, '확증편향', '자신의 신념이나 가치관과 일치하는 정보만을 찾고, 반대되는 정보는 무시하는 사고방식')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (1, '선전하다 (宣傳)', '주의나 주장, 사물의 존재, 효능 따위를 많은 사람이 알고 이해하도록 잘 설명하여 널리 알리다.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (2, '직조하다 (織造)', '기계나 베틀 따위로 피륙을 짜다.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (2, '수탈하다 (收奪)', '강제로 빼앗다.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (3, '기인하다 (起因)', '어떠한 것에 원인을 두다.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (3, '토대 (土臺)', '어떤 사물이나 사업의 밑바탕이 되는 기초를 비유적으로 이르는 말.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (4, '순증 (純增)', '실질적인 순전한 증가.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (4, '자명하다 (自明)', '설명하거나 증명하지 아니하여도 저절로 알 만큼 명백하다.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (5, '도식 (圖式)', '사물의 구조, 관계, 변화 상태 따위를 일정한 양식으로 나타낸 양식.')",
            "INSERT INTO $TABLE_WORDS ($COLUMN_NEWS_ID, $COLUMN_WORD, $COLUMN_MEANING) VALUES (5, '미려하다', '아름답고 곱다.')"
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

    fun addBookmark(word: String, meaning: String) {
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_WORD, word)
        contentValues.put(COLUMN_MEANING, meaning)

        db.insert(TABLE_BOOKMARKS, null, contentValues)
        db.close()
    }

    fun getAllBookmarks(): List<Bookmark> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKMARKS,
            arrayOf(COLUMN_WORD, COLUMN_MEANING),
            null, null, null, null, null
        )

        val bookmarkList = mutableListOf<Bookmark>()
        while (cursor.moveToNext()) {
            val word = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WORD))
            val meaning = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEANING))
            bookmarkList.add(Bookmark(word, meaning))
        }
        cursor.close()
        db.close()
        return bookmarkList
    }

    fun isBookmarkExist(word: String): Boolean { // 중복 검증
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKMARKS,
            arrayOf(COLUMN_WORD),
            "$COLUMN_WORD = ?",
            arrayOf(word),
            null, null, null
        )

        val exists = cursor.count > 0  // 단어가 존재하면 true, 아니면 false
        cursor.close()
        db.close()

        return exists
    }

    fun clearAllBookmarks() { // 북마크 초기화
        val db = writableDatabase
        db.delete(TABLE_BOOKMARKS, null, null)
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "news_database.db"
        private const val DATABASE_VERSION = 3
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