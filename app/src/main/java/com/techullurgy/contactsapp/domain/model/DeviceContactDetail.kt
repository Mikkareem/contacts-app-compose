package com.techullurgy.contactsapp.domain.model

data class DeviceContactDetail(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phones: List<PhoneInformation>,
    val emails: List<EmailInformation>,
    val events: List<EventInformation>,
) {
    val displayName = "$firstName $lastName"

    val initials: String
        get() = if (lastName.isNotBlank()) "${firstName.take(1)}${lastName.take(1)}" else firstName.take(
            2
        )
}

sealed interface PhoneInformation {
    data class HomePhone(val phoneNo: String): PhoneInformation
    data class WorkPhone(val phoneNo: String): PhoneInformation
    data class MobilePhone(val phoneNo: String): PhoneInformation
}

sealed interface EmailInformation {
    data class HomeEmail(val email: String): EmailInformation
    data class WorkEmail(val email: String): EmailInformation
    data class MobileEmail(val email: String): EmailInformation
}

sealed interface EventInformation {
    data class BirthdayEvent(val eventDate: String): EventInformation
    data class AnniversaryEvent(val eventDate: String): EventInformation
}