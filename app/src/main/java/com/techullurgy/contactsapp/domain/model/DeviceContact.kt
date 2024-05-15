package com.techullurgy.contactsapp.domain.model

data class DeviceContact(
    val contactId: String,
    val displayName: String
) {
    val initials: String
        get() = (displayName.getFirstCharAfterFirstSpace() ?: displayName.take(2)).uppercase()

    private fun String.getFirstCharAfterFirstSpace(): String? =
        trim()
            .takeIf {
                indexOf(" ") != -1
            }
            ?.let {
                val names = split(" ").filter { it.isNotBlank() }.take(2)
                names[0][0] + "" + names[1][0]
            }
}