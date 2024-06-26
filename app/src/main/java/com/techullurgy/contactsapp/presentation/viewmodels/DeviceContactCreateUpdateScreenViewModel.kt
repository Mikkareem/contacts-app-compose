package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.ContactsRepository
import com.techullurgy.contactsapp.domain.model.DeviceContactRequest
import com.techullurgy.contactsapp.domain.model.EmailInformation
import com.techullurgy.contactsapp.domain.model.EmailType
import com.techullurgy.contactsapp.domain.model.EventInformation
import com.techullurgy.contactsapp.domain.model.EventType
import com.techullurgy.contactsapp.domain.model.PhoneInformation
import com.techullurgy.contactsapp.domain.model.PhoneType
import kotlinx.coroutines.launch

class DeviceContactCreateUpdateScreenViewModel(
    private val contactsRepository: ContactsRepository
): ViewModel() {

    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phones = mutableStateListOf(mutableStateOf(PhoneType.HOME.value to ""))
    var emails = mutableStateListOf(mutableStateOf(EmailType.HOME.value to ""))
    var events = mutableStateListOf(mutableStateOf(EventType.BIRTHDAY.value to ""))
    var error by mutableStateOf("")

    private var _contactId = ""

    fun loadForUpdate(contactId: String) {
        _contactId = contactId
        viewModelScope.launch {
            when (val result = contactsRepository.getDeviceContactDetailFor(contactId)) {
                is ServiceResult.Failure -> {
                    error = result.error
                }

                is ServiceResult.Success -> {
                    firstName = result.data.firstName
                    lastName = result.data.lastName
                    events = result.data.events.map { info ->
                        when (info) {
                            is EventInformation.AnniversaryEvent -> {
                                mutableStateOf(EventType.ANNIVERSARY.value to info.eventDate)
                            }

                            is EventInformation.BirthdayEvent -> {
                                mutableStateOf(EventType.BIRTHDAY.value to info.eventDate)
                            }
                        }
                    }.toMutableStateList()
                    phones = result.data.phones.map { info ->
                        when (info) {
                            is PhoneInformation.HomePhone -> {
                                mutableStateOf(PhoneType.HOME.value to info.phoneNo)
                            }

                            is PhoneInformation.MobilePhone -> {
                                mutableStateOf(PhoneType.MOBILE.value to info.phoneNo)
                            }

                            is PhoneInformation.WorkPhone -> {
                                mutableStateOf(PhoneType.WORK.value to info.phoneNo)
                            }
                        }
                    }.toMutableStateList()
                    emails = result.data.emails.map { info ->
                        when (info) {
                            is EmailInformation.HomeEmail -> {
                                mutableStateOf(EmailType.HOME.value to info.email)
                            }

                            is EmailInformation.MobileEmail -> {
                                mutableStateOf(EmailType.MOBILE.value to info.email)
                            }

                            is EmailInformation.WorkEmail -> {
                                mutableStateOf(EmailType.WORK.value to info.email)
                            }
                        }
                    }.toMutableStateList()
                }
            }
        }
    }

    private fun validate(): Boolean {
        val set = HashSet<String>()
        phones.filter { it.value.second.isNotEmpty() }.forEach {
            if (!set.add(it.value.first)) {
                error = "${it.value.first} Phone is available more than once"
                return false
            }
        }
        set.clear()
        emails.filter { it.value.second.isNotEmpty() }.forEach {
            if (!set.add(it.value.first)) {
                error = "${it.value.first} Email is available more than once"
                return false
            }
        }
        set.clear()
        events.filter { it.value.second.isNotEmpty() }.forEach {
            if (!set.add(it.value.first)) {
                error = "${it.value.first} Event is available more than once"
                return false
            }
        }

        error = ""
        return true
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
                emails.add(mutableStateOf(EmailType.HOME.value to ""))
            }

            DeviceContactCreateUpdateScreenEvent.OnAddEvent -> {
                events.add(mutableStateOf(EventType.BIRTHDAY.value to ""))
            }

            DeviceContactCreateUpdateScreenEvent.OnAddPhone -> {
                phones.add(mutableStateOf(PhoneType.HOME.value to ""))
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
                if (validate()) {
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
            }

            is DeviceContactCreateUpdateScreenEvent.OnUpdate -> {
                if (validate()) {
                    viewModelScope.launch {
                        val request = DeviceContactRequest(
                            firstName = firstName,
                            lastName = lastName,
                            phone = phones.filter { it.value.second.isNotBlank() }.map { it.value },
                            email = emails.filter { it.value.second.isNotBlank() }.map { it.value },
                            event = events.filter { it.value.second.isNotBlank() }.map { it.value }
                        )
                        contactsRepository.saveDeviceContact(
                            contactId = _contactId,
                            request = request
                        )
                        event.onSaved()
                    }
                }
            }

            is DeviceContactCreateUpdateScreenEvent.OnEmailTypeChange -> {
                emails[event.index].value = event.value to emails[event.index].value.second
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