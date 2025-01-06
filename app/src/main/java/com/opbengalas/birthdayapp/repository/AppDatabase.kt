package com.opbengalas.birthdayapp.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.opbengalas.birthdayapp.dao.ContactDao
import com.opbengalas.birthdayapp.models.Contact
import com.opbengalas.birthdayapp.util.Converters

@Database(entities = [Contact::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) // Necesario para LocalDate
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}