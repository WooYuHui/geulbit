package com.example.gulbit.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.gulbit.model.Word

class WordDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_WORDS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_WORD TEXT NOT NULL,
                $COLUMN_MEANING TEXT NOT NULL,
                $COLUMN_EXAMPLE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableQuery)

        //  단어 20개 추가
        insertInitialWords(db)
    }

    private fun insertInitialWords(db: SQLiteDatabase) {
        val words = listOf(
            Word("강구하다", "좋은 대책이나 방법을 궁리해 찾아내거나 세우다.", "문제를 해결하기 위해 대책을 강구하다."),
            Word("고심하다", "어떤 일을 해결하기 위해 깊이 생각하다.", "나는 문제를 해결하기 위해 오랜 시간 고심했다."),
            Word("감탄하다", "훌륭한 것을 보고 놀라다.", "그의 뛰어난 연기에 모두 감탄했다."),
            Word("점철되다", "관련이 있는 상황이나 사실 등이 서로 이어짐.", "그의 삶은 고독과 방랑으로 점철돼 있다."),
            Word("통상적인", "특별하지 않고 보통인", "통상적인 서두는 생략하고 바로 본론으로 들어가겠습니다."),
            Word("기인하다", "어떤한 것에 원인을 두다.", "롤스는 수치심을 능력의 탁월성의 부재 및 상실에 따른 자연적 수치심과 인격의 탁월성의 부재 및 상실에서 기인하는 도덕적 수치심으로 구분한다."),
            Word("선전하다", "주의나 주장, 사물의 존재, 효능 따위를 많은 사람이 알고 이해하도록 잘 설명하여 널리 알리다.", "유대인을 악마화하는 선전을 펼쳤다."),
            Word("박해", "못살게 굴어서 해롭게 함.", "다수의 미국인이 무고한 이웃의 박해를 침묵 속에서 지켜보기만 했다."),
            Word("여과", "주로 부정적인 요소를 걸러 내는 과정을 비유적으로 이르는 말.", "유발 하라리는 모든 정보가 여과없이 흐르도록 내버려두면 진실이 지는 경향이 있다고 충고한다."),
            Word("직조하다", "기계나 베틀 따위로 피륙을 짜다.", "순면을 직조하여 만든 담요는 얇으면서도 보온 효과가 뛰어나다."),
            Word("항거하다", "순종하지 아니하고 맞서서 반항하다.", "독립운동가이신 할아버지는 일제의 수탈에 항거하시다 옥고를 치르셨다."),
            Word("대관절", "'대체','도대체'라는 뜻으로, 이유를 물을 때나 감탄사로 사용.", "대관절 왜 그런 결정을 내렸는지 이해할 수가 없다."),
            Word("미덥다", "믿음성이 있고 믿음직스럽다.", "그의 미더운 태도는 팀원들에게 신뢰를 심어주었습니다."),
            Word("그윽하다", "깊숙하고 아늑하며 고요한 상태를 의미.", "산속 깊은 곳에 위치한 사찰은 그윽한 부위기로 방문객들의 마음을 평온하게 만들었습니다."),
            Word("괴리", "꼭두각시, 허수아비", "그는 외세의 괴뢰로 지목되어 많은 비판을 받았다."),
            Word("근자", "요사이, 요즈음", "근자에 들어 건강이 많이 좋아졌다."),
            Word("염가", "싼값", "염가 판매로 인해 손님들이 몰려들었다."),
            Word("생경하다", "익숙하지 않아 어색하다.", "전혀 낯선 세계의 풍경이 생경한 느낌으로 다가왔다."),
            Word("기만하다", "남을 속여 넘기다.","그는 정직하여 남을 기만하지 않는다."),
            Word("도외시하다", "상관하지 아니하거나 무시하다.", "그의 말을 도외시해서는 안된다.")
        )

        words.forEach { word ->
            val values = ContentValues().apply {
                put(COLUMN_WORD, word.word)
                put(COLUMN_MEANING, word.meaning)
                put(COLUMN_EXAMPLE, word.example)
            }

            // 파라미터 바인딩을 사용하여 데이터를 삽입
            db.insert(TABLE_WORDS, null, values)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WORDS")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "words.db"
        private const val DATABASE_VERSION = 2  // ⚠ 버전 올려야 onUpgrade 실행됨
        const val TABLE_WORDS = "words"
        const val COLUMN_ID = "id"
        const val COLUMN_WORD = "word"
        const val COLUMN_MEANING = "meaning"
        const val COLUMN_EXAMPLE = "example"
    }
}
