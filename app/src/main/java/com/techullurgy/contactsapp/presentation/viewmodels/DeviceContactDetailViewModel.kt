package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.presentation.models.DeviceContactDetailScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeviceContactDetailViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    private val _state = MutableStateFlow(DeviceContactDetailScreenState())
    val state = _state.asStateFlow()

    fun getContactDetail(contactId: String) {
        viewModelScope.launch {
            when(val result = contactsRepository.getDeviceContactDetailFor(contactId = contactId)) {
                is ServiceResult.Success -> {
                    _state.update {
                        it.copy(
                            contactDetail = result.data,
                            error = ""
                        )
                    }
                }
                is ServiceResult.Failure -> {
                    _state.update {
                        it.copy(
                            error = ""
                        )
                    }
                }
            }
        }
    }
}