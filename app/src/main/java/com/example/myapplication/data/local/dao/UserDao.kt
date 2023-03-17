package com.example.myapplication.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myapplication.data.local.pojo.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUser(id: Long): User

    @Query("SELECT * FROM user")
    fun getUserList(): LiveData<List<User>>

    @Delete
    fun deleteUser(vararg user: User)

    @Query("DELETE FROM user")
    fun truncate()
}