package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.data.remote.UserApi
import com.example.myapplication.other.Constants
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: UserApi
) {

    fun insertUser(vararg user: User) = dao.insertUser(*user)

    fun getUserList(): LiveData<PagingData<User>> {

        UserPagingSource(dao, api)

        return Pager(
            config = PagingConfig(Constants.PAGING_SIZE),
            pagingSourceFactory = { dao.getUserList() }
        ).liveData
    }

    fun getUser(id: Long) = dao.getUser(id)

    fun truncate() = dao.truncate()
    fun deleteUser(vararg user: User) = dao.deleteUser(*user)
}