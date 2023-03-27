package com.example.myapplication.data.repository

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.data.remote.UserApi
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPagingSource(private val api: UserApi) :
    RxPagingSource<Int, User>() {

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return null
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, User>> {
        val page = params.key ?: 1
        var data = emptyList<User>()
        var totalPages = 0

        return Single.fromObservable(api.getUserList(page))
            .subscribeOn(Schedulers.io())
            .map {
                if (it.isSuccessful) {
                    data = it.body()?.data ?: emptyList()
                    totalPages = it.body()?.totalPage ?: 0
                }

                LoadResult.Page(
                    data = data,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (page == totalPages) null else page + 1
                ) as LoadResult<Int, User>
            }.onErrorReturn {
                LoadResult.Error(it)
            }
    }
}