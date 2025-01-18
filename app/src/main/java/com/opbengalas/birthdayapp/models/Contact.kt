package com.opbengalas.birthdayapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val birthdayDate: LocalDate,
    val phoneNumber: String = "",
    val description: String = "",
    val notificationTone: String? = null,
    val notificationDuration: Int = 0,
    val notificationRepeat: Boolean = false,
    val videoUrl: String? = null, // Campo opcional para video
    val profileImage: String = "default_profile_image"
)