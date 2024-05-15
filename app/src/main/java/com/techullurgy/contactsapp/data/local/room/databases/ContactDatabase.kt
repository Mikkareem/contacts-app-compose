package com.techullurgy.contactsapp.data.local.room.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.techullurgy.contactsapp.data.local.room.daos.LocalContactsDao
import com.techullurgy.contactsapp.data.local.room.entities.LocalContact

@Database(
    entities = [LocalContact::class],
    version = 1
)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun localContactsDao(): LocalContactsDao
}