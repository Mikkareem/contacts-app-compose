package com.techullurgy.contactsapp.domain.model

data class DeviceContactRequest(
    val firstName: String,
    val lastName: String,
    val phone: List<Pair<String, String>>,
    val email: List<Pair<String, String>>,
    val event: List<Pair<String, String>>,
)