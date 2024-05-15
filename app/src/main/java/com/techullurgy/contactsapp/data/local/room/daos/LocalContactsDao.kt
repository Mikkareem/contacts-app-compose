package com.techullurgy.contactsapp.data.local.room.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.techullurgy.contactsapp.data.local.room.entities.LocalContact
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalContactsDao {

    @Query("Select * from LocalContact")
    fun getAllContacts(): Flow<List<LocalContact>>

    @Query("Select * from LocalContact where id = :contactId")
    suspend fun getContactById(contactId: Long): LocalContact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveContact(vararg contact: LocalContact)

    @Query("Select * from LocalContact where displayName LIKE '%' || :query || '%' order by displayName ASC")
    suspend fun getSearchResultsFor(query: String): List<LocalContact>
}