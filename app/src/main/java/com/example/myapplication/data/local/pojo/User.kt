package com.example.myapplication.data.local.pojo

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
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
    @PrimaryKey(autoGenerate = true)
    var id: Long? = 0L // this field is not mandatory for creating a user object

    @IgnoredOnParcel
    @Embedded
    var image: Bitmap? = null // this field is not mandatory for creating a user object
}