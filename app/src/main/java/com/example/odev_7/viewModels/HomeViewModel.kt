package com.example.odev_7.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.odev_7.databases.DB
import com.example.odev_7.models.Note

class HomeViewModel: ViewModel() {

    var saveNotestatus = MutableLiveData<Boolean>()
    var notes = MutableLiveData<MutableList<Note>>()
    var db : DB? = null

    fun getAllNotes(){
        db?.let {database ->
            notes.value = database.getAllNotes()
        }
    }

    fun saveNote(title : String, description : String, createdAt : String){
        db?.let { database ->
            val addResult = database.addNote(title,description, createdAt)
            saveNotestatus.value = addResult > 0
        }
    }

}