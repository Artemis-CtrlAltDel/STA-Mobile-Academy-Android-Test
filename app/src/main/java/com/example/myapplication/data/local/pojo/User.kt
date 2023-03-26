package com.example.myapplication.data.local.pojo

import android.net.Uri
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(
    @SerializedName("first_name")
    val fname: String,
    @SerializedName("last_name")
    val lname: String,
    val email: String,
    val phone: String? = "",
    val fax: String? = "",

    val country: String? = "",
    val city: String? = "",
    val job: String? = "",
    val bio: String? = "",

    var avatar: String? = null
): Parcelable {

    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null

    @IgnoredOnParcel
    var image: Uri? = null

    @IgnoredOnParcel
    var joinedTimestamp: Long = System.currentTimeMillis()

    @IgnoredOnParcel
    var isLocal: Boolean = false
}