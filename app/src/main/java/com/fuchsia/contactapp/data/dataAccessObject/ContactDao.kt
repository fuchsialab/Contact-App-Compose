package com.fuchsia.contactapp.data.dataAccessObject

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.fuchsia.contactapp.data.entities.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Upsert
    suspend fun upsertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM CONTACTS_TABLE")
    fun getContacts(): Flow<List<Contact>>


}