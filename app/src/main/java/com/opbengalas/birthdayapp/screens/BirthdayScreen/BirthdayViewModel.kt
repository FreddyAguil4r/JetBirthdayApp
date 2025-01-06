package com.opbengalas.birthdayapp.screens.BirthdayScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opbengalas.birthdayapp.models.Contact
import com.opbengalas.birthdayapp.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val _listContact = MutableStateFlow<List<Contact>>(emptyList())
    val listContact: StateFlow<List<Contact>> get() = _listContact

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> get() = _date

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description

    private var isAscending = true
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        viewModelScope.launch {
            repository.allContacts.collect { contacts ->
                _listContact.value = contacts
            }
        }
    }

    fun addContactName(newName: String) {
        _name.value = newName
    }

    fun addContactDate(newDate: String) {
        _date.value = newDate
    }

    fun addContactDescription(newDescription: String) {
        _description.value = newDescription
    }

    fun addBirthdayFromInput() {
        if (_name.value.isNotBlank() && _date.value.isNotBlank()) {
            try {
                val parsedDate = LocalDate.parse(_date.value, dateFormatter)
                val newContact = Contact(
                    name = _name.value,
                    birthdayDate = parsedDate,
                    description = _description.value
                )
                viewModelScope.launch {
                    repository.insert(newContact)
                }
                _name.value = ""
                _date.value = ""
                _description.value = ""
            } catch (e: DateTimeParseException) {
                // Handle invalid date format
            }
        }
    }

    fun toggleSortOrder() {
        isAscending = !isAscending
        viewModelScope.launch {
            val sortedContacts = if (isAscending) {
                _listContact.value.sortedBy { it.birthdayDate }
            } else {
                _listContact.value.sortedByDescending { it.birthdayDate }
            }
            _listContact.value = sortedContacts
        }
    }
}