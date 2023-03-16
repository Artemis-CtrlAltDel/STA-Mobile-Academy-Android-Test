package com.example.myapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.local.pojo.User

@Dao
interface UserDao {

    @Insert
    fun insertUser(vararg user: User)

    @Query("SELECT * FROM user WHERE fname = :fname AND lname = :lname")
    fun getUser(fname: String, lname: String): User

    @Query("SELECT * FROM user")
    fun getUserList(): LiveData<List<User>>

    @Delete
    fun deleteUser(vararg user: User)

    @Query("DELETE FROM user")
    fun truncate()
}