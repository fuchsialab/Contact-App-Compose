package com.fuchsia.contactapp.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

object DatabaseInit {

    private var database: ContactDatabase? = null
    fun getDatabase(context: Context) : ContactDatabase {
        if (database == null){
            database =   Room.databaseBuilder(context, ContactDatabase::class.java,
                "contacts_database")
                .setJournalMode(RoomDatabase.JournalMode.TRUNCATE).build()
        }
        return database!!
    }
}



