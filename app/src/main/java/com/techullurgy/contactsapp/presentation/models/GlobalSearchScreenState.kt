package com.techullurgy.contactsapp.presentation.models

import com.techullurgy.contactsapp.domain.model.DeviceContact
import com.techullurgy.contactsapp.domain.model.RandomContact


data class GlobalSearchScreenState(
    val randoms: List<RandomContact> = emptyList(),
    val devices: List<DeviceContact> = emptyList()
)