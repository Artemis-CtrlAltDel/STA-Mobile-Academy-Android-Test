package com.example.myapplication.other

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

fun Long.formatDate(): String =
    SimpleDateFormat("dd/MM/yyyy HH:mm a", Locale.US).format(Date(this))

fun ImageView.loadImage(context: Context, avatar: String?, uri: Uri?, default: Int) =
    avatar?.let {
        Glide.with(context).load(Uri.parse(avatar)).into(this)
    } ?: run { Glide.with(context).load(uri ?: default).into(this) }