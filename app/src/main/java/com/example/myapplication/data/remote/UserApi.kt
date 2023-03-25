package com.example.myapplication.data.remote

import com.example.myapplication.data.remote.response.UserResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("users")
    fun getUserList(@Query("page") page: Int): Observable<Response<UserResponse>>
}