package com.opbengalas.birthdayapp.repository

import com.opbengalas.birthdayapp.dao.ContactDao
import com.opbengalas.birthdayapp.models.Contact
import kotlinx.coroutines.flow.Flow


class ContactRepository(private val contactDao: ContactDao) {

    val allContacts: Flow<List<Contact>> = contactDao.getAllContacts()

    suspend fun insert(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun delete(contact: Contact) {
        contactDao.deleteContact(contact)
    }
}