package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.presentation.models.DeviceContactsListScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeviceContactsViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    private val _state = MutableStateFlow(DeviceContactsListScreenState())
    val state = _state.asStateFlow()

    init {
        fetchContacts()
    }

    private fun fetchContacts() {
        viewModelScope.launch {
            when(val result = contactsRepository.getDeviceContacts()) {
                is ServiceResult.Success -> {
                    _state.update {
                        it.copy(
                            contacts = result.data,
                            error = ""
                        )
                    }
                }
                is ServiceResult.Failure -> {
                    _state.update {
                        it.copy(
                            error = result.error
                        )
                    }
                }
            }
        }
    }
}