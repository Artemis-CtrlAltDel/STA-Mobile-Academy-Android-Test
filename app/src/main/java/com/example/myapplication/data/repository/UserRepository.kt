package com.example.myapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.data.remote.UserApi
import com.example.myapplication.other.Constants
import com.example.myapplication.other.Resource
import java.io.IOException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val dao: UserDao,
    private val api: UserApi
) {

    fun insertUser(vararg user: User) = dao.insertUser(*user)

    fun getUserList(): LiveData<Resource<PagingData<User>>> = liveData {

        try {
            val result =
                api.getUserList().map {
                    if (it.isSuccessful) {
                        val users = it.body()?.data ?: listOf()
                        dao.insertUser(*users.toTypedArray())

                        emit(Resource.Success(
                            Pager(
                                config = PagingConfig(Constants.PAGING_SIZE)
                            ) { dao.getUserList() }.liveData.value!!
                        ))
                        return@map
                    }
                }.doOnError { emit(Resource.Error(it.message ?: "Something went wrong")) }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Please check you internet connection"))
        }

        Pager(
            config = PagingConfig(Constants.PAGING_SIZE)
        ) { dao.getUserList() }.liveData
    }

    fun getUser(id: Long) = dao.getUser(id)

    fun truncate() = dao.truncate()
    fun deleteUser(vararg user: User) = dao.deleteUser(*user)
}