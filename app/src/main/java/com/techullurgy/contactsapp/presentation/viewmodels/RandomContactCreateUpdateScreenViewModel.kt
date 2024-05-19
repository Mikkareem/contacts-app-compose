package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.local.room.entities.LocalContact
import com.techullurgy.contactsapp.domain.ContactsRepository
import kotlinx.coroutines.launch

class RandomContactCreateUpdateScreenViewModel(
    private val contactsRepository: ContactsRepository
) : ViewModel() {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phone by mutableStateOf("")
    var cell by mutableStateOf("")
    var email by mutableStateOf("")
    var gender by mutableStateOf("Male")
    var profileUrl by mutableStateOf("")
    var error by mutableStateOf("")

    private var _contactId = -1L
    private var _page = -1

    fun loadForUpdate(contactId: Long) {
        _contactId = contactId
        viewModelScope.launch {
            contactsRepository.getRandomContactDetailFor(contactId)?.let { contact ->
                firstName = contact.firstName
                lastName = contact.lastName
                phone = contact.phone
                cell = contact.cell
                email = contact.email
                gender = contact.gender
                profileUrl = contact.profileMedium
                _page = contact.page
            }
        }
    }

    fun onEvent(event: RandomContactCreateUpdateScreenEvent) {
        when (event) {
            is RandomContactCreateUpdateScreenEvent.OnCellChange -> {
                cell = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnFirstNameChange -> {
                firstName = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnGenderChange -> {
                gender = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnLastNameChange -> {
                lastName = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnPhoneChange -> {
                phone = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnEmailChange -> {
                email = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnProfileUrlChange -> {
                profileUrl = event.value
            }

            is RandomContactCreateUpdateScreenEvent.OnCreate -> {
                viewModelScope.launch {
                    create()
                    event.onSaved()
                }
            }

            is RandomContactCreateUpdateScreenEvent.OnUpdate -> {
                viewModelScope.launch {
                    update()
                    event.onSaved()
                }
            }
        }
    }

    private suspend fun create() {
        val localContact = LocalContact(
            id = 0,
            firstName = firstName,
            lastName = lastName,
            gender = gender,
            email = email,
            phone = phone,
            cell = cell,
            picture = profileUrl,
            page = contactsRepository.getLastAvailablePage()
        )
        contactsRepository.saveRandomContact(localContact)
    }

    private suspend fun update() {
        val localContact = LocalContact(
            id = _contactId,
            firstName = firstName,
            lastName = lastName,
            gender = gender,
            email = email,
            phone = phone,
            cell = cell,
            picture = profileUrl,
            page = _page
        )
        contactsRepository.saveRandomContact(localContact)
    }
}

sealed interface RandomContactCreateUpdateScreenEvent {
    data class OnFirstNameChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnLastNameChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnPhoneChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnCellChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnEmailChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnGenderChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnProfileUrlChange(val value: String) : RandomContactCreateUpdateScreenEvent
    data class OnCreate(val onSaved: () -> Unit) : RandomContactCreateUpdateScreenEvent
    data class OnUpdate(val onSaved: () -> Unit) : RandomContactCreateUpdateScreenEvent
}