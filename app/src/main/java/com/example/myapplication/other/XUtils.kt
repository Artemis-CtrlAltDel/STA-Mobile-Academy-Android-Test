package com.example.myapplication.other

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDate(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.US).format(Date(this))

fun ImageView.loadImage(context: Context, uri: Uri?, default: Int) =
    uri?.let {
        Glide.with(context).load(uri).into(this)
    } ?: run { Glide.with(context).load(default).into(this) }