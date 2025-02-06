package com.fuchsia.contactapp.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.fuchsia.contactapp.data.dataAccessObject.ContactDao
import com.fuchsia.contactapp.data.entities.Contact

@Database(entities = [Contact::class], version = 2, exportSchema = true, autoMigrations = [
    AutoMigration(from = 1, to = 2)
])
abstract class ContactDatabase : RoomDatabase() {

    abstract val dao: ContactDao


}