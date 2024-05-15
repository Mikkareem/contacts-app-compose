package com.techullurgy.contactsapp.domain.model

data class RandomContact(
    val id: Long,
    val name: String,
    val phone: String,
    val gender: String,
    val email: String,
    val profileMedium: String
)
