package com.zoho.randomuser.di

import android.content.Context
import com.zoho.randomuser.data.AppDatabase
import com.zoho.randomuser.data.RandomUserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideRandomUserDao(appDatabase: AppDatabase): RandomUserDao {
        return appDatabase.randomUserDao()
    }

}