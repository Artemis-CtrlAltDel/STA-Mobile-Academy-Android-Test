package com.example.myapplication.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.local.pojo.User
@TypeConverters(Converters::class)
@Database(
    entities = [User::class],
    version = 10
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}