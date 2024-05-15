package com.techullurgy.contactsapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteContact(
    val gender: String,
    val name: RemoteContactName,
    val email: String,
    val phone: String,
    val cell: String,
    val picture: RemoteContactPicture
)