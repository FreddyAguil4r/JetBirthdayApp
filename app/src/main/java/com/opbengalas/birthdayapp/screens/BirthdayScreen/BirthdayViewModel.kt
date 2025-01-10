package com.opbengalas.birthdayapp.screens.BirthdayScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opbengalas.birthdayapp.models.Contact
import com.opbengalas.birthdayapp.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel @Inject constructor(private val repository: ContactRepository) :
    ViewModel() {

    private val _listContact = MutableStateFlow<List<Contact>>(emptyList())
    val listContact: StateFlow<List<Contact>> get() = _listContact

    private val _sortedContacts = MutableStateFlow<List<Contact>>(emptyList())
    val sortedContacts: StateFlow<List<Contact>> get() = _sortedContacts

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _date = MutableStateFlow("")
    val date: StateFlow<String> get() = _date

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> get() = _description

    //Variables de notificaciones

    private val _todayBirthdays = MutableStateFlow<List<Contact>>(emptyList())
    val todayBirthdays: StateFlow<List<Contact>> get() = _todayBirthdays

    private val _notifyContacts = MutableSharedFlow<Contact>(replay = 0)
    val notifyContacts: SharedFlow<Contact> get() = _notifyContacts

    private var isAscending = true
    private val today = LocalDate.now()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    init {
        viewModelScope.launch {
            repository.allContacts.collect { contacts ->
                _listContact.value = contacts
                updateSortedContacts()
                updateTodayBirthdays()
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

    fun deleteContact(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact)
            _listContact.value = _listContact.value.filter { it.id != contact.id }
        }
    }

    fun updateContact(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
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
                viewModelScope.launch { repository.insert(newContact) }
                _name.value = ""
                _date.value = ""
                _description.value = ""
            } catch (e: DateTimeParseException) {
                Log.e("AddContact", "Formato de fecha inválido: ${e.message}")
                // Muestra un mensaje al usuario
            }
        } else {
            Log.e("AddContact", "Nombre o fecha vacíos")
        }
    }

    private fun updateSortedContacts() {
        _sortedContacts.value = if (isAscending) {
            _listContact.value.sortedBy { it.birthdayDate }
        } else {
            _listContact.value.sortedByDescending { it.birthdayDate }
        }
    }

    fun toggleSortOrder() {
        isAscending = !isAscending
        updateSortedContacts()
    }

    private fun updateTodayBirthdays() {
        val todayBirthdays = _listContact.value.filter { contact ->
            contact.birthdayDate.month == today.month && contact.birthdayDate.dayOfMonth == today.dayOfMonth
        }
        _todayBirthdays.value = todayBirthdays

        viewModelScope.launch {
            todayBirthdays.forEach { _notifyContacts.emit(it) }
        }
    }
}