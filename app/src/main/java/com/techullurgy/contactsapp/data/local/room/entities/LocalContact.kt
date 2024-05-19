package com.techullurgy.contactsapp.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalContact(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val firstName: String,
    val lastName: String,
    val displayName: String = "$firstName $lastName",
    val gender: String,
    val phone: String,
    val cell: String,
    val email: String,
    val picture: String,
    val page: Int,
)
