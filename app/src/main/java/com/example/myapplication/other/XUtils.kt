package com.example.myapplication.other

import java.text.SimpleDateFormat
import java.util.*

object XUtils {

    fun Long.formatDate(): String = SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.US).format(Date(this))
}