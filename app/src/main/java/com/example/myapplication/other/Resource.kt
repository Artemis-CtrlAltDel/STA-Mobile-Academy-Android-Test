package com.example.myapplication.other

sealed class Resource<T>(
    var loading: Boolean = false,
    var error: String? = null,
    var data: T? = null
) {
    class Loading<T>(data: T?) : Resource<T>(loading = true, data = data)
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: String) : Resource<T>(error = error)
}
