package com.example.myapplication.data.repository

import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.pojo.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao
) {

    fun getUserList() = dao.getUserList()
    fun insertUser(vararg user: User) = dao.insertUser(*user)
    fun getUser(fname: String, lname: String) = dao.getUser(fname, lname)

    fun truncate() = dao.truncate()
    fun deleteUser(vararg user: User) = dao.deleteUser(*user)
}