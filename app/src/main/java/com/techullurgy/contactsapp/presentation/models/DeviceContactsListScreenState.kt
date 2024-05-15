package com.techullurgy.contactsapp.presentation.models

import com.techullurgy.contactsapp.domain.model.DeviceContact

data class DeviceContactsListScreenState(
    val contacts: List<DeviceContact> = emptyList(),
    val error: String = ""
)