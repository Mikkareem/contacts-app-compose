package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.domain.model.DeviceContactRequest
import kotlinx.coroutines.launch

class DeviceContactCreateUpdateScreenViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phones = mutableStateListOf(mutableStateOf("Home" to ""))
    var emails = mutableStateListOf(mutableStateOf("Home" to ""))
    var events = mutableStateListOf(mutableStateOf("Birthday" to ""))

    private var _contactId = ""

    fun loadForUpdate(contactId: String) {
        _contactId = contactId
//        viewModelScope.launch {
//            when(val result = contactsRepository.getDeviceContactDetailFor(contactId)) {
//                is ServiceResult.Failure -> {
//                    _state.update {
//                        it.copy(
//                            error = result.error
//                        )
//                    }
//                }
//                is ServiceResult.Success -> {
//                    _state.update {
//                        it.copy(
//                            contactId = contactId,
//                            firstName = result.data.firstName,
//                            lastName = result.data.lastName,
//                        )
//                    }
//                    events = result.data.events.map { info ->
//                        when(info) {
//                            is EventInformation.AnniversaryEvent -> {
//                                "Anniversary" to info.eventDate
//                            }
//                            is EventInformation.BirthdayEvent -> {
//                                "Birthday" to info.eventDate
//                            }
//                        }
//                    }.toMutableStateList()
//                    phones = result.data.phones.map { info ->
//                        when(info) {
//                            is PhoneInformation.HomePhone -> {
//                                "Home" to info.phoneNo
//                            }
//                            is PhoneInformation.MobilePhone -> {
//                                "Mobile" to info.phoneNo
//                            }
//                            is PhoneInformation.WorkPhone -> {
//                                "Work" to info.phoneNo
//                            }
//                        }
//                    }.toMutableStateList()
//                    emails = result.data.emails.map { info ->
//                        when(info) {
//                            is EmailInformation.HomeEmail -> {
//                                "Home" to info.email
//                            }
//                            is EmailInformation.MobileEmail -> {
//                                "Mobile" to info.email
//                            }
//                            is EmailInformation.WorkEmail -> {
//                                "Work" to info.email
//                            }
//                        }
//                    }.toMutableStateList()
//                }
//            }
//        }
    }

    fun onEvent(event: DeviceContactCreateUpdateScreenEvent) {
        when(event) {
            is DeviceContactCreateUpdateScreenEvent.OnEmailChange -> {
                emails[event.index].value = emails[event.index].value.first to event.value
            }

            is DeviceContactCreateUpdateScreenEvent.OnEventChange -> {
                events[event.index].value = events[event.index].value.first to event.value
            }

            is DeviceContactCreateUpdateScreenEvent.OnFirstNameChange -> {
                firstName = event.value
            }

            is DeviceContactCreateUpdateScreenEvent.OnLastNameChange -> {
                lastName = event.value
            }

            is DeviceContactCreateUpdateScreenEvent.OnPhoneChange -> {
                phones[event.index].value = phones[event.index].value.first to event.value
            }

            DeviceContactCreateUpdateScreenEvent.OnAddEmail -> {
                emails.add(mutableStateOf("Home" to ""))
            }

            DeviceContactCreateUpdateScreenEvent.OnAddEvent -> {
                events.add(mutableStateOf("Birthday" to ""))
            }

            DeviceContactCreateUpdateScreenEvent.OnAddPhone -> {
                phones.add(mutableStateOf("Home" to ""))
            }

            is DeviceContactCreateUpdateScreenEvent.OnCloseEmail -> {
                emails.removeAt(event.index)
            }
            is DeviceContactCreateUpdateScreenEvent.OnCloseEvent -> {
                events.removeAt(event.index)
            }
            is DeviceContactCreateUpdateScreenEvent.OnClosePhone -> {
                phones.removeAt(event.index)
            }

            is DeviceContactCreateUpdateScreenEvent.OnCreate -> {
                viewModelScope.launch {
                    val request = DeviceContactRequest(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phones.filter { it.value.second.isNotEmpty() }.map { it.value },
                        email = emails.filter { it.value.second.isNotEmpty() }.map { it.value },
                        event = events.filter { it.value.second.isNotEmpty() }.map { it.value }
                    )
                    contactsRepository.saveDeviceContact(request)
                    event.onSaved()
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnUpdate -> {
                viewModelScope.launch {
                    val request = DeviceContactRequest(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phones.filter { it.value.second.isNotEmpty() }.map { it.value },
                        email = emails.filter { it.value.second.isNotEmpty() }.map { it.value },
                        event = events.filter { it.value.second.isNotEmpty() }.map { it.value }
                    )
                    contactsRepository.saveDeviceContact(contactId = _contactId, request = request)
                    event.onSaved()
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnEmailTypeChange -> {
                emails[event.index].value = event.value to events[event.index].value.second
            }

            is DeviceContactCreateUpdateScreenEvent.OnEventTypeChange -> {
                events[event.index].value = event.value to events[event.index].value.second
            }

            is DeviceContactCreateUpdateScreenEvent.OnPhoneTypeChange -> {
                phones[event.index].value = event.value to phones[event.index].value.second
            }
        }
    }
}

sealed interface DeviceContactCreateUpdateScreenEvent {
    data class OnFirstNameChange(val value: String): DeviceContactCreateUpdateScreenEvent
    data class OnLastNameChange(val value: String): DeviceContactCreateUpdateScreenEvent
    data class OnPhoneChange(val value: String, val index: Int) :
        DeviceContactCreateUpdateScreenEvent

    data class OnEmailChange(val value: String, val index: Int) :
        DeviceContactCreateUpdateScreenEvent

    data class OnEventChange(val value: String, val index: Int) :
        DeviceContactCreateUpdateScreenEvent

    data class OnPhoneTypeChange(val value: String, val index: Int) :
        DeviceContactCreateUpdateScreenEvent

    data class OnEmailTypeChange(val value: String, val index: Int) :
        DeviceContactCreateUpdateScreenEvent

    data class OnEventTypeChange(val value: String, val index: Int) :
        DeviceContactCreateUpdateScreenEvent
    data class OnClosePhone(val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnCloseEmail(val index: Int): DeviceContactCreateUpdateScreenEvent
    data class OnCloseEvent(val index: Int): DeviceContactCreateUpdateScreenEvent
    data object OnAddPhone: DeviceContactCreateUpdateScreenEvent
    data object OnAddEmail: DeviceContactCreateUpdateScreenEvent
    data object OnAddEvent: DeviceContactCreateUpdateScreenEvent
    data class OnCreate(val onSaved: () -> Unit): DeviceContactCreateUpdateScreenEvent
    data class OnUpdate(val onSaved: () -> Unit) : DeviceContactCreateUpdateScreenEvent
}