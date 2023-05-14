package com.example.odev_7.viewModels

import android.content.ContentValues
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.odev_7.databases.DB
import com.example.odev_7.models.Note

class NoteDetailViewModel : ViewModel() {

    var note = MutableLiveData<Note>()
    var db : DB? = null
    var deleteResult = MutableLiveData<Int>()
    var updateResult = MutableLiveData<Int>()

    fun getNoteById(nid : Int){
        db?.let {database->
            note.value = database.getNoteById(nid)
        }
    }

    fun updateNote(nid : Int, cv : ContentValues){
        db?.let { database ->
            val title = cv.getAsString("title")
            val description = cv.getAsString("description")
            val modifiedAt = cv.getAsString("modified_at")
            updateResult.value = database.updateNote(nid, title,description,modifiedAt)
        }
    }

    fun deleteNote(nid : Int){
        db?.let { database->
            deleteResult.value = database.deleteNote(nid)
        }
    }

}