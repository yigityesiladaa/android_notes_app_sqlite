package com.example.odev_7.databases

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.odev_7.models.Note

class DB(context : Context) : SQLiteOpenHelper(context, DBName,null, Version) {

    companion object{
        private const val DBName = "notes"
        private const val Version = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val noteTable = "CREATE TABLE \"$DBName\" (\n" +
                "\t\"nid\"\tINTEGER,\n" +
                "\t\"title\"\tTEXT,\n" +
                "\t\"description\"\tTEXT,\n" +
                "\t\"created_at\"\tTEXT,\n" +
                "\t\"modified_at\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"nid\" AUTOINCREMENT)\n" +
                ");"
        db?.execSQL(noteTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val noteTableDrop = "DROP TABLE IF EXISTS $DBName"
        db?.execSQL(noteTableDrop)
        onCreate(db)
    }

    fun addNote(title : String, detail : String, createdAt : String) : Long{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title",title)
        cv.put("description",detail)
        cv.put("created_at",createdAt)

        val effectRowCount = db.insert(DBName,null,cv)
        db.close()
        return effectRowCount
    }

    fun deleteNote(nid : Int) : Int{
        val db = this.readableDatabase
        val status = db.delete(DBName,"nid = $nid",null)
        db.close()
        return status
    }

    fun updateNote(nid : Int, title : String, detail : String, modifiedAt : String) : Int{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put("title",title)
        cv.put("description",detail)
        cv.put("modified_at",modifiedAt)

        val status = db.update(DBName,cv,"nid = $nid",null)
        db.close()
        return status
    }


    fun getNoteById(nid : Int) : Note?{
        val db = this.readableDatabase
        var note : Note? = null
        val cursor = db.rawQuery("SELECT * from $DBName WHERE nid = ?",arrayOf(nid.toString()))
        while (cursor.moveToNext()){
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val createdAt = cursor.getString(3)
            val modifiedAt = cursor.getString(4)

            note = Note(nid,title,description,createdAt,modifiedAt)
        }

        db.close()
        return note
    }

    fun getAllNotes() : MutableList<Note>{
        val db = this.readableDatabase
        var notes = mutableListOf<Note>()
        val cursor = db.query(DBName,null,null,null,null,null,"created_at")

        while (cursor.moveToNext()){
            val nid = cursor.getInt(0)
            val title = cursor.getString(1)
            val description = cursor.getString(2)
            val createdAt = cursor.getString(3)
            val modifiedAt = cursor.getString(4)

            val note = Note(nid,title,description,createdAt,modifiedAt)

            notes.add(note)
        }

        db.close()
        return notes
    }

}