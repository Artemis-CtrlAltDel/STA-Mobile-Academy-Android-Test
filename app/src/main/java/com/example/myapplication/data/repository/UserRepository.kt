package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.pojo.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao
) {

    fun insertUser(user: User) = dao.insertUser(user)

    fun getUserList() = dao.getUserList()
    fun getUser(id: Long) = dao.getUser(id)

    fun truncate() = dao.truncate()
    fun deleteUser(vararg user: User) = dao.deleteUser(*user)
}