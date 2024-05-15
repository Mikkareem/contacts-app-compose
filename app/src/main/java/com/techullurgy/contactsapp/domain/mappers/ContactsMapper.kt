package com.techullurgy.contactsapp.domain.mappers

import com.techullurgy.contactsapp.data.local.room.entities.LocalContact
import com.techullurgy.contactsapp.domain.model.RandomContact


fun LocalContact.toRandomContact(): RandomContact = RandomContact(
    id = id,
    name = displayName,
    phone = phone,
    gender = gender,
    email = email,
    profileMedium = picture
)