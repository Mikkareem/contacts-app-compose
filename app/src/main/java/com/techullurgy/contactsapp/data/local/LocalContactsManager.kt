package com.techullurgy.contactsapp.data.local

import com.techullurgy.contactsapp.data.local.room.daos.LocalContactsDao
import com.techullurgy.contactsapp.data.local.room.entities.LocalContact
import com.techullurgy.contactsapp.data.remote.model.RemoteContact

class LocalContactsManager(
    private val localContactsDao: LocalContactsDao
) {
    fun getAllContacts() = localContactsDao.getAllContacts()

    suspend fun saveContacts(contacts: List<RemoteContact>, currentPage: Int) {
        val localContacts = contacts.map { contact ->
            LocalContact(
                id = 0,
                firstName = contact.name.first,
                lastName = contact.name.last,
                gender = contact.gender,
                phone = contact.phone,
                cell = contact.cell,
                email = contact.email,
                picture = contact.picture.medium,
                page = currentPage
            )
        }

        localContactsDao.saveContact(*localContacts.toTypedArray())
    }

    suspend fun saveContact(localContact: LocalContact) = localContactsDao.saveContact(localContact)

    suspend fun getContactDetailFor(contactId: Long): LocalContact? = localContactsDao.getContactById(contactId = contactId)

    suspend fun getSearchResultsFor(query: String) = localContactsDao.getSearchResultsFor(query)

    suspend fun getLastAvailablePage(): Int = localContactsDao.getAvailableMaxPage()
}