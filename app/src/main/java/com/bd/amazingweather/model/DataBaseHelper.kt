package com.bd.amazingweather.model

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "weather.db"
        private const val DATABASE_VERSION = 1

        // 表名和字段
        private const val TABLE_WEATHER_HISTORY = "weather_history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CITY = "city"
        private const val COLUMN_CUR_TEMP = "curTemp"
        private const val COLUMN_TEMP_MIN = "tempMin"
        private const val COLUMN_TEMP_MAX = "tempMax"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_TIMESTAMP = "timestamp"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_WEATHER_TABLE = """
            CREATE TABLE IF NOT EXISTS $TABLE_WEATHER_HISTORY (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CITY TEXT NOT NULL,
                $COLUMN_CUR_TEMP TEXT,
                $COLUMN_TEMP_MIN TEXT,
                $COLUMN_TEMP_MAX TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_TIMESTAMP INTEGER NOT NULL
            )
        """.trimIndent()

        db?.execSQL(CREATE_WEATHER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addWeather(wi: WeatherInfo): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_CITY, wi.city)
            put(COLUMN_CUR_TEMP, wi.curTemp)
            put(COLUMN_TEMP_MIN, wi.tempMin)
            put(COLUMN_TEMP_MAX, wi.tempMax)
            put(COLUMN_DESCRIPTION, wi.description)
            put(COLUMN_TIMESTAMP, System.currentTimeMillis())
        }

        return db.insert(TABLE_WEATHER_HISTORY, null, values)
    }

    // 查询城市天气（30分钟内有效）
    fun getWeather(city: String): Cursor? {
        val db = readableDatabase

        val columns = arrayOf(
            COLUMN_CITY,
            COLUMN_CUR_TEMP,
            COLUMN_TEMP_MIN,
            COLUMN_TEMP_MAX,
            COLUMN_DESCRIPTION,
            COLUMN_TIMESTAMP
        )

        val selection = "$COLUMN_CITY = ?"
        val selectionArgs = arrayOf(city)

        val cursor = db.query(
            TABLE_WEATHER_HISTORY,
            columns,
            selection,
            selectionArgs,
            null, null, null
        )

        if (cursor.moveToFirst()) {
            val timestampIndex = cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP)
            if (timestampIndex >= 0) {
                val timestamp = cursor.getLong(timestampIndex)
                val now = System.currentTimeMillis()
                if (now - timestamp <= 30 * 60 * 1000) { // 半小时内有效
                    return cursor
                }
            }
        }

        cursor.close()
        return null
    }

    fun updateWeather(wi: WeatherInfo): Long {
        val db = writableDatabase
        val city = wi.city
        val now = System.currentTimeMillis()

        val selection = "$COLUMN_CITY = ?"
        val selectionArgs = arrayOf(city)

        val cursor = db.query(
            TABLE_WEATHER_HISTORY,
            null,
            selection,
            selectionArgs,
            null, null, null
        )

        val result: Long = if (cursor.moveToFirst()) {
            val values = ContentValues().apply {
                put(COLUMN_CITY, wi.city)
                put(COLUMN_CUR_TEMP, wi.curTemp)
                put(COLUMN_TEMP_MIN, wi.tempMin)
                put(COLUMN_TEMP_MAX, wi.tempMax)
                put(COLUMN_DESCRIPTION, wi.description)
                put(COLUMN_TIMESTAMP, now)
            }

            db.update(TABLE_WEATHER_HISTORY, values, selection, selectionArgs).toLong()
        } else {
            addWeather(wi)
        }

        cursor.close()
        return result
    }
}