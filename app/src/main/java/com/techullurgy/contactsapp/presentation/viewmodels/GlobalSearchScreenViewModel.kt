package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.domain.mappers.toRandomContact
import com.techullurgy.contactsapp.presentation.models.GlobalSearchScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GlobalSearchScreenViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    private val _state = MutableStateFlow(GlobalSearchScreenState())
    val state = _state.asStateFlow()

    fun search(query: String) {
        viewModelScope.launch {
            launch {
                val deviceContacts = contactsRepository.getDeviceContactsSearchResults(query)
                _state.update {
                    it.copy(
                        devices = deviceContacts
                    )
                }
            }
            launch {
                val localContacts = contactsRepository.getLocalContactsSearchResults(query)
                _state.update {
                    it.copy(
                        randoms = localContacts.map { t -> t.toRandomContact() }
                    )
                }
            }
        }
    }
}
