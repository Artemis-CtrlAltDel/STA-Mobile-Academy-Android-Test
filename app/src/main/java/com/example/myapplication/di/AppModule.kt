package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.local.UserDao
import com.example.myapplication.data.remote.UserApi
import com.example.myapplication.data.repository.UserRepository
import com.example.myapplication.other.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, AppDatabase::class.java, "MyApplicationDB")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun providesUserDao(db: AppDatabase) =
        db.userDao()

    @Singleton
    @Provides
    fun providesUserApi() =
        Retrofit.Builder()
            .baseUrl(Constants.USER_API_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
}