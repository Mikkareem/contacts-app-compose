package com.techullurgy.contactsapp.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class AdditionalInfo(
    val seed: String,
    val results: Int,
    val page: Int,
    val version: String
)