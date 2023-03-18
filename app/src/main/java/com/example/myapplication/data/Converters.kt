package com.example.myapplication.data

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromUri(value: Uri): String = value.toString()

    @TypeConverter
    fun toUri(value: String): Uri = value.toUri()
}