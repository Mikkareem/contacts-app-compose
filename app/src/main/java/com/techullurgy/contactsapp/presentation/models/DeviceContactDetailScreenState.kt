package com.techullurgy.contactsapp.presentation.models

import com.techullurgy.contactsapp.domain.model.DeviceContactDetail

data class DeviceContactDetailScreenState(
    val contactDetail: DeviceContactDetail? = null,
    val error: String = ""
)
