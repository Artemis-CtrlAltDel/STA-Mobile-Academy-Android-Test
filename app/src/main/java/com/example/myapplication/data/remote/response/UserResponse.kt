package com.example.myapplication.data.remote.response

import com.example.myapplication.data.local.pojo.User

data class UserResponse(
    val page: Int,
    val perPage: Int,
    val total: Int,
    val totalPage: Int,
    val data: List<User>
)