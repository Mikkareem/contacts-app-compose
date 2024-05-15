package com.techullurgy.contactsapp.presentation.models

data class DeviceContactCreateUpdateScreenState(
    val contactId: String = "",
    val error: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phones: List<Pair<String, String>> = listOf("Home" to ""),
    val emails: List<Pair<String, String>> = listOf("Home" to ""),
    val events: List<Pair<String, String>> = listOf("Birthday" to ""),
    val editable: Boolean = true
) {
    val creatable: Boolean
        get() = firstName.isNotBlank() && phones.any { it.second.isNotBlank() }
}