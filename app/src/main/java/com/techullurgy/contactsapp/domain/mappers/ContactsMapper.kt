package com.techullurgy.contactsapp.domain.mappers

import com.techullurgy.contactsapp.data.local.room.entities.LocalContact
import com.techullurgy.contactsapp.domain.model.RandomContact


fun LocalContact.toRandomContact(): RandomContact = RandomContact(
    id = id,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    gender = gender,
    email = email,
    cell = cell,
    profileMedium = picture,
    page = page
)