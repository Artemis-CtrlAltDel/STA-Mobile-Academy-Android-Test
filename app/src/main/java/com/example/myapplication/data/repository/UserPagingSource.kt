package com.example.myapplication.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.local.pojo.User
import com.example.myapplication.data.remote.UserApi
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class UserPagingSource(private val dao: UserDao, private val api: UserApi) :
    PagingSource<Int, User>() {
    companion object {
        private const val TAG = "UserPagingSource"
        private val compositeDisposable = CompositeDisposable()
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        try {

            val currentLoadingPageKey = params.key ?: 1
            var data = emptyList<User>()

            // TODO fix data retrieval from api
            val disposable = api.getUserList(currentLoadingPageKey)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    if (it.isSuccessful) {
                        data = it.body()?.data ?: emptyList()
                    }
                }) { Log.d(TAG, "load: ${it.message}") }

            compositeDisposable.add(disposable)

            val actualData = mutableListOf<User>()
            actualData.addAll(data)

            dao.insertUser(*actualData.toTypedArray())

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            compositeDisposable.dispose()

            return LoadResult.Page(
                data = actualData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}