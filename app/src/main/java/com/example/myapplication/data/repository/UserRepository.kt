package com.example.myapplication.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.other.Constants
import java.util.concurrent.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao
) {

    fun insertUser(user: User) = dao.insertUser(user)

    fun getUserList(): LiveData<PagingData<User>> =
        Pager(
            config = PagingConfig(Constants.PAGING_SIZE)
        ) { dao.getUserList() }.liveData

    fun getUser(id: Long) = dao.getUser(id)

    fun truncate() = dao.truncate()
    fun deleteUser(vararg user: User) = dao.deleteUser(*user)
}