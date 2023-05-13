package com.example.odev_7.interfaces

import android.content.ContentValues

interface INoteDetailListener : IFragmentListener{

    fun getNote(nid : Int)

    fun updateNote(nid : Int, cv: ContentValues)

    fun deleteNote(nid : Int)

}