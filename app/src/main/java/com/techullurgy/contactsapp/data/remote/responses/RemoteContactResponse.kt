package com.techullurgy.contactsapp.data.remote.responses

import com.techullurgy.contactsapp.data.remote.model.AdditionalInfo
import com.techullurgy.contactsapp.data.remote.model.RemoteContact
import kotlinx.serialization.Serializable

@Serializable
data class RemoteContactResponse(
    val results: List<RemoteContact>,
    val info: AdditionalInfo
)
