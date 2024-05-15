package com.techullurgy.contactsapp.presentation.models

import com.techullurgy.contactsapp.domain.model.RandomContact

data class RandomContactDetailScreenState(
    val randomContact: RandomContact? = null,
    val error: String = ""
)
