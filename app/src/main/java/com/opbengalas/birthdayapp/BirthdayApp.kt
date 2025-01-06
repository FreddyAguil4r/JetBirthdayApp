package com.opbengalas.birthdayapp

import android.app.Application
import androidx.room.Room
import com.opbengalas.birthdayapp.repository.AppDatabase
import com.opbengalas.birthdayapp.repository.ContactRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BirthdayApp : Application() {

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "birthday_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    val repository: ContactRepository by lazy {
        ContactRepository(database.contactDao())
    }
}