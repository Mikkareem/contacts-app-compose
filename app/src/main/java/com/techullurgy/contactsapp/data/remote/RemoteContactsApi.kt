package com.techullurgy.contactsapp.data.remote

import com.techullurgy.contactsapp.data.remote.responses.RemoteContactResponse
import com.techullurgy.contactsapp.data.utils.ServiceResult

interface RemoteContactsApi {
    suspend fun getPaginatedContacts(page: Int): ServiceResult<RemoteContactResponse>
}
