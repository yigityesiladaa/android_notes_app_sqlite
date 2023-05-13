package com.example.odev_7.models

import android.os.Parcelable

data class Note(
    val nid : Int,
    val title : String,
    val description : String,
    val createdAt : String,
    val modifiedAt : String?,
)
