package com.fuchsia.contactapp.data.repository

import com.fuchsia.contactapp.data.dataAccessObject.ContactDao
import com.fuchsia.contactapp.data.entities.Contact
import kotlinx.coroutines.flow.Flow

class ContactRepository(
    private val dao: ContactDao
) {
    suspend fun upsertContact(contact: Contact) {
        dao.upsertContact(contact)
    }
    suspend fun deleteContact(contact: Contact) {
        dao.deleteContact(contact)
    }
    suspend fun getContacts()= dao.getContacts()


}