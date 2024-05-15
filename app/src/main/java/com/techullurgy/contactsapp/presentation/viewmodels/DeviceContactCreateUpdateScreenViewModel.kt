package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.domain.model.DeviceContactRequest
import com.techullurgy.contactsapp.domain.model.EmailInformation
import com.techullurgy.contactsapp.domain.model.EventInformation
import com.techullurgy.contactsapp.domain.model.PhoneInformation
import com.techullurgy.contactsapp.presentation.models.DeviceContactCreateUpdateScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeviceContactCreateUpdateScreenViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    private val _state = MutableStateFlow(DeviceContactCreateUpdateScreenState())
    val state = _state.asStateFlow()

    fun loadForUpdate(contactId: String) {
        viewModelScope.launch {
            when(val result = contactsRepository.getDeviceContactDetailFor(contactId)) {
                is ServiceResult.Failure -> {
                    _state.update {
                        it.copy(
                            error = result.error
                        )
                    }
                }
                is ServiceResult.Success -> {
                    _state.update {
                        it.copy(
                            contactId = contactId,
                            firstName = result.data.firstName,
                            lastName = result.data.lastName,
                            events = result.data.events.map { info ->
                                when(info) {
                                    is EventInformation.AnniversaryEvent -> {
                                        "Anniversary" to info.eventDate
                                    }
                                    is EventInformation.BirthdayEvent -> {
                                        "Birthday" to info.eventDate
                                    }
                                }
                            },
                            phones = result.data.phones.map { info ->
                                when(info) {
                                    is PhoneInformation.HomePhone -> {
                                        "Home" to info.phoneNo
                                    }
                                    is PhoneInformation.MobilePhone -> {
                                        "Mobile" to info.phoneNo
                                    }
                                    is PhoneInformation.WorkPhone -> {
                                        "Work" to info.phoneNo
                                    }
                                }
                            },
                            emails = result.data.emails.map { info ->
                                when(info) {
                                    is EmailInformation.HomeEmail -> {
                                        "Home" to info.email
                                    }
                                    is EmailInformation.MobileEmail -> {
                                        "Mobile" to info.email
                                    }
                                    is EmailInformation.WorkEmail -> {
                                        "Work" to info.email
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: DeviceContactCreateUpdateScreenEvent) {
        when(event) {
            is DeviceContactCreateUpdateScreenEvent.OnEmailChange -> {
                _state.update {
                    val currentIndex = event.index
                    require(currentIndex >= 0 && currentIndex <= it.emails.size)
                    if(currentIndex == it.emails.size) {
                        it.copy(
                            emails = it.emails.toMutableList().apply {
                                add(
                                    event.type to event.value
                                )
                            }.toList()
                        )
                    } else {
                        it.copy(
                            emails = it.emails.mapIndexed { index, pair ->
                                if(index == currentIndex) {
                                    event.type to event.value
                                } else pair
                            }
                        )
                    }
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnEventChange -> {
                _state.update {
                    val currentIndex = event.index
                    require(currentIndex >= 0 && currentIndex <= it.events.size)
                    if(currentIndex == it.events.size) {
                        it.copy(
                            events = it.events.toMutableList().apply {
                                add(
                                    event.type to event.value
                                )
                            }.toList()
                        )
                    } else {
                        it.copy(
//                            events = it.events.mapIndexed { index, pair ->
//                                if(index == currentIndex) {
//                                    event.type to event.value
//                                } else pair
//                            },
                            events = it.events.toMutableList().apply {
                                set(currentIndex, event.type to event.value)
                            }.toList()
                        )
                    }
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnFirstNameChange -> {
                _state.update {
                    it.copy(
                        firstName = event.value
                    )
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnLastNameChange -> {
                _state.update {
                    it.copy(
                        lastName = event.value
                    )
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnPhoneChange -> {
                _state.update {
                    val currentIndex = event.index
                    require(currentIndex >= 0 && currentIndex <= it.phones.size)
                    if(currentIndex == it.phones.size) {
                        it.copy(
                            phones = it.phones.toMutableList().apply {
                                add(
                                    event.type to event.value
                                )
                            }.toList()
                        )
                    } else {
                        it.copy(
                            phones = it.phones.mapIndexed { index, pair ->
                                if(index == currentIndex) {
                                    event.type to event.value
                                } else pair
                            }
                        )
                    }
                }
            }

            DeviceContactCreateUpdateScreenEvent.OnAddEmail -> {
                _state.update {
                    it.copy(
                        emails = it.emails.toMutableList().apply {
                            add("Home" to "")
                        }.toList()
                    )
                }
            }

            DeviceContactCreateUpdateScreenEvent.OnAddEvent -> {
                _state.update {
                    it.copy(
                        events = it.events.toMutableList().apply {
                            add("Birthday" to "")
                        }.toList()
                    )
                }
            }

            DeviceContactCreateUpdateScreenEvent.OnAddPhone -> {
                _state.update {
                    it.copy(
                        phones = it.phones.toMutableList().apply {
                            add("Home" to "")
                        }.toList()
                    )
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnCloseEmail -> {
                _state.update {
                    it.copy(
                        emails = it.emails.toMutableList().apply {
                            removeAt(event.index)
                        }.toList()
                    )
                }
            }
            is DeviceContactCreateUpdateScreenEvent.OnCloseEvent -> {
                _state.update {
                    it.copy(
                        events = it.events.toMutableList().apply {
                            removeAt(event.index)
                        }.toList()
                    )
                }
            }
            is DeviceContactCreateUpdateScreenEvent.OnClosePhone -> {
                _state.update {
                    it.copy(
                        phones = it.phones.toMutableList().apply {
                            removeAt(event.index)
                        }.toList()
                    )
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnCreate -> {
                viewModelScope.launch {
                    val request = DeviceContactRequest(
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        phone = _state.value.phones.filter { it.second.isNotEmpty() },
                        email = _state.value.emails.filter { it.second.isNotEmpty() },
                        event = _state.value.events.filter { it.second.isNotEmpty() }
                    )
                    contactsRepository.saveDeviceContact(request)
                    event.onSaved()
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnUpdate -> {
                viewModelScope.launch {
                    val request = DeviceContactRequest(
                        firstName = _state.value.firstName,
                        lastName = _state.value.lastName,
                        phone = _state.value.phones.filter { it.second.isNotEmpty() },
                        email = _state.value.emails.filter { it.second.isNotEmpty() },
                        event = _state.value.events.filter { it.second.isNotEmpty() }
                    )
                    contactsRepository.saveDeviceContact(contactId = event.contactId, request = request)
                    event.onSaved()
                }
            }
        }
    }
}

sealed interface DeviceContactCreateUpdateScreenEvent {
    data class OnFirstNameChange(val value: String): DeviceContactCreateUpdateScreenEvent
    data class OnLastNameChange(val value: String): DeviceContactCreateUpdateScreenEvent
    data class OnPhoneChange(val value: String, val type: String, val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnEmailChange(val value: String, val type: String, val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnEventChange(val value: String, val type: String, val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnClosePhone(val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnCloseEmail(val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnCloseEvent(val index: Int): DeviceContactCreateUpdateScreenEvent
    data object OnAddPhone: DeviceContactCreateUpdateScreenEvent
    data object OnAddEmail: DeviceContactCreateUpdateScreenEvent
    data object OnAddEvent: DeviceContactCreateUpdateScreenEvent
    data class OnCreate(val onSaved: () -> Unit): DeviceContactCreateUpdateScreenEvent
    data class OnUpdate(val contactId: String, val onSaved: () -> Unit): DeviceContactCreateUpdateScreenEvent
}