package com.techullurgy.contactsapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteContactPicture(
    val large: String,
    val medium: String,
    val thumbnail: String
)
