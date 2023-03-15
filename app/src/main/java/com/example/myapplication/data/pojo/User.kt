package com.example.myapplication.data.pojo

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val fname: String,
    val lname: String,
    val email: String,
    val phone: String,
    val fax: String,

    val country: String,
    val city: String,
    val job: String,
    val bio: String,
): Parcelable {

    @IgnoredOnParcel
    var image: Bitmap? = null // image field is not mandatory
}