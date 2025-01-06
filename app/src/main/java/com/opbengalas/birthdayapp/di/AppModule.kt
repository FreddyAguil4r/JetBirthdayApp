package com.opbengalas.birthdayapp.di

import android.content.Context
import androidx.room.Room
import com.opbengalas.birthdayapp.dao.ContactDao
import com.opbengalas.birthdayapp.repository.AppDatabase
import com.opbengalas.birthdayapp.repository.ContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "birthday_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideContactDao(appDatabase: AppDatabase): ContactDao {
        return appDatabase.contactDao()
    }

    @Provides
    @Singleton
    fun provideContactRepository(contactDao: ContactDao): ContactRepository {
        return ContactRepository(contactDao)
    }
}