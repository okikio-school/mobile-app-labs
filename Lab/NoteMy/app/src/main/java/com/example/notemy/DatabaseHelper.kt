package com.example.notemy

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

private const val DATABASE_NAME = "NOTES_RECORD"
private const val TABLE_NAME = "STUDENT_DATA"

private const val HIDDEN_COL = "rowid"
private const val COL_1 = "ID"
private const val COL_2 = "TITLE"
private const val COL_3 = "SUBTITLE"
private const val COL_4 = "DESCRIPTION"
private const val COL_5 = "COLOR"

class DatabaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // Create a virtual table to store the data required for full-text search
        // Note: virtual tables only support TEXT data types for columns,
        // in addition to the `rowid` column which acts as an autoincrement primary key integer
        db.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS $TABLE_NAME USING fts4($COL_2, $COL_3, $COL_4, $COL_5)");
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME");
        onCreate(db);
    }

    fun listData(): MutableList<Map<String, Any>> {
        return query("SELECT $HIDDEN_COL as $COL_1, * FROM $TABLE_NAME")
    }

    fun getData(id: String): MutableList<Map<String, Any>> {
        return query("SELECT $HIDDEN_COL as $COL_1, * FROM $TABLE_NAME WHERE $COL_1 = ?", arrayOf(id))
    }

    // Search query using the MATCH operator to perform full-text search
    fun searchData(queryString: String): MutableList<Map<String, Any>> {
        return query("SELECT $HIDDEN_COL as $COL_1, * FROM $TABLE_NAME WHERE $TABLE_NAME MATCH ?", arrayOf(queryString))
    }

    private fun query(query: String, params: Array<String> = emptyArray()): MutableList<Map<String, Any>> {
        val notesRecord = mutableListOf<Map<String, Any>>()
        val db = writableDatabase

        val cursor = db.rawQuery(query, params)
        if (cursor.moveToFirst()) {
            do {
                val note = mapOf(
                    "id" to cursor.getInt(cursor.getColumnIndexOrThrow(COL_1)),
                    "title" to cursor.getString(cursor.getColumnIndexOrThrow(COL_2)),
                    "subtitle" to cursor.getString(cursor.getColumnIndexOrThrow(COL_3)),
                    "description" to cursor.getString(cursor.getColumnIndexOrThrow(COL_4)),
                    "color" to cursor.getString(cursor.getColumnIndexOrThrow(COL_5))
                )
                notesRecord.add(note)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return notesRecord
    }

    fun insertData(title: String, subtitle: String, description: String, color: String): Boolean {
        if (title.isEmpty()) return false;

        val db = writableDatabase;
        val values = ContentValues();
        values.put(COL_2, title);
        values.put(COL_3, subtitle);
        values.put(COL_4, description);
        values.put(COL_5, color);

        val insertion = db.insert(TABLE_NAME, null, values);
        return insertion != -1L;
    }

}