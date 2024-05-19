package com.techullurgy.contactsapp.presentation.models

import com.techullurgy.contactsapp.domain.model.RandomContact

data class RandomContactsListScreenState(
    val contacts: List<RandomContact> = emptyList(),
    val pageError: String = "",
    val error: String = "",
    val pageLoading: Boolean = false,
    val loadMoreLoading: Boolean = false
)
