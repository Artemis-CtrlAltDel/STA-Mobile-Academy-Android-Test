package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.local.dao.UserDao
import com.example.myapplication.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesAppDatabase(@ApplicationContext app: Context) =
        Room.databaseBuilder(app, AppDatabase::class.java, "MyApplicationDB")
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun providesUserDao(db: AppDatabase) = db.userDao()

    @Singleton
    @Provides
    fun providesUserRepository(dao: UserDao) = UserRepository(dao)
}