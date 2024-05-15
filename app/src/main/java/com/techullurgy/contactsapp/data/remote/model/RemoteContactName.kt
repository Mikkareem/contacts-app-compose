package com.techullurgy.contactsapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RemoteContactName(
    val title: String,
    val first: String,
    val last: String
)
