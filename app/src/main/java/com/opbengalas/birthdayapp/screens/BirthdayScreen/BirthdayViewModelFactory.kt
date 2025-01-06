package com.opbengalas.birthdayapp.screens.BirthdayScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opbengalas.birthdayapp.repository.ContactRepository

class BirthdayViewModelFactory(private val repository: ContactRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BirthdayViewModel::class.java)) {
            return BirthdayViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}