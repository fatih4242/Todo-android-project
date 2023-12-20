package com.tokersoftware.todokt.classes

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tokersoftware.todokt.activity.main.model.TodoModel

class DatabaseHelper(val context: Context) : SQLiteOpenHelper(context,DatabaseHelper.DATABASE_NAME,null,DatabaseHelper.DATABASE_VERSION) {
    private val TABLE_NAME="TODO"
    private val COL_ID = "id"
    private val COL_TITLE = "title"
    private val COL_DATE = "date"
    private val COL_MESSAGE = "message"
    companion object {
        private val DATABASE_NAME = "SQLITE_DATABASE"//database adı
        private val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_TITLE  VARCHAR(256),$COL_DATE  VARCHAR(256),$COL_MESSAGE  VARCHAR(256))"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    fun insertData(todo:TodoModel) : Boolean {
        val sqliteDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_TITLE , todo.title)
        contentValues.put(COL_DATE, todo.date)
        contentValues.put(COL_MESSAGE, todo.message)

        val result = sqliteDB.insert(TABLE_NAME,null,contentValues)

        return if(result != -1L)  true else false
    }

    @SuppressLint("Range")
    fun readData():MutableList<TodoModel>{
        val todoList = mutableListOf<TodoModel>()
        val sqliteDB = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = sqliteDB.rawQuery(query,null)
        if(result.moveToFirst()){
            do {
                val todo = TodoModel()
                todo.id = result.getString(result.getColumnIndex(COL_ID)).toInt()
                todo.title = result.getString(result.getColumnIndex(COL_TITLE))
                todo.date = result.getString(result.getColumnIndex(COL_DATE))
                todo.message = result.getString(result.getColumnIndex(COL_MESSAGE))
                todoList.add(todo)

            }while (result.moveToNext())
        }
        result.close()
        sqliteDB.close()
        return todoList
    }

    fun deleteDataByID(id: String){
        val sqliteDB = this.writableDatabase
        sqliteDB.delete(TABLE_NAME,"$COL_ID=?", arrayOf(id))
        sqliteDB.close()
    }


    @SuppressLint("Range")
    fun updateTodo(todo:TodoModel) : Boolean {
        try {
            val db = this.writableDatabase
            val query = "SELECT * FROM $TABLE_NAME"
            val result = db.rawQuery(query,null)

            val cv = ContentValues()
            cv.put(COL_TITLE, todo.title)
            cv.put(COL_DATE, todo.date)
            cv.put(COL_MESSAGE, todo.message)

            db.update(TABLE_NAME,cv, "$COL_ID=?", arrayOf(todo.id.toString()))

            result.close()
            db.close()
            return true
        } catch (e: java.lang.Exception) {
            e.stackTrace
        }
        return false
    }
}