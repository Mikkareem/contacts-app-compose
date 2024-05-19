package com.techullurgy.contactsapp.domain.model

data class RandomContact(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val phone: String,
    val cell: String,
    val gender: String,
    val email: String,
    val profileMedium: String,
    val page: Int = -1
) {
    val displayName = "$firstName $lastName"

    val initials =
        if (lastName.isNotBlank()) "${firstName.take(1)}${lastName.take(1)}" else firstName.take(2)
}
