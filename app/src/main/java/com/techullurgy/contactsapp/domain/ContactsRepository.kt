package com.techullurgy.contactsapp.domain

import com.techullurgy.contactsapp.data.device.DeviceContactsManager
import com.techullurgy.contactsapp.data.local.LocalContactsManager
import com.techullurgy.contactsapp.data.local.room.entities.LocalContact
import com.techullurgy.contactsapp.data.remote.RemoteContactsApi
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.mappers.toRandomContact
import com.techullurgy.contactsapp.domain.model.DeviceContactRequest
import com.techullurgy.contactsapp.domain.model.RandomContact

class ContactsRepository(
    private val remoteContactsApi: RemoteContactsApi,
    private val deviceContactsManager: DeviceContactsManager,
    private val localContactsManager: LocalContactsManager
) {
    fun getRandomContacts() = localContactsManager.getAllContacts()

    suspend fun getPaginatedRandomContacts(page: Int): ServiceResult<Unit> {
        return when(val result = remoteContactsApi.getPaginatedContacts(page)) {
            is ServiceResult.Failure -> result
            is ServiceResult.Success -> {
                val contacts = result.data.results
                localContactsManager.saveContacts(contacts = contacts, currentPage = page)
                ServiceResult.Success(Unit)
            }
        }
    }

    suspend fun getDeviceContacts() = deviceContactsManager.fetchAllContacts()

    suspend fun getDeviceContactDetailFor(contactId: String) = deviceContactsManager.getContactDetailFor(contactId = contactId)

    suspend fun getRandomContactDetailFor(contactId: Long): RandomContact? {
        return localContactsManager.getContactDetailFor(contactId = contactId)?.toRandomContact()
    }

    suspend fun getLastAvailablePage() = localContactsManager.getLastAvailablePage()

    suspend fun saveDeviceContact(request: DeviceContactRequest) = deviceContactsManager.addNewContact(request)
    suspend fun saveDeviceContact(contactId: String, request: DeviceContactRequest) = deviceContactsManager.editContact(contactId, request)
    suspend fun saveRandomContact(contact: LocalContact) =
        localContactsManager.saveContact(localContact = contact)

    suspend fun getDeviceContactsSearchResults(query: String) = deviceContactsManager.getSearchResultsFor(query)
    suspend fun getLocalContactsSearchResults(query: String) = localContactsManager.getSearchResultsFor(query)
}