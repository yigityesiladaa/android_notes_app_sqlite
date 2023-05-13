package com.example.odev_7.interfaces

interface IHomeListener : IFragmentListener {

    fun saveNote(title : String, description : String, createdAt : String)

}