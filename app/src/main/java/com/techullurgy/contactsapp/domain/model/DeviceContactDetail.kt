package com.techullurgy.contactsapp.domain.model

data class DeviceContactDetail(
    val id: String,
    val firstName: String,
    val lastName: String,
    val phones: List<PhoneInformation>,
    val emails: List<EmailInformation>,
    val events: List<EventInformation>,
) {
    val initials: String
        get() = (firstName.getFirstCharAfterFirstSpace() ?: firstName.take(2)).uppercase()

    private fun String.getFirstCharAfterFirstSpace(): String? =
        trim().
            takeIf {
                it.indexOf(" ") != -1
            }
            ?.let {
                val names = split(" ").filter { it.isNotBlank() }.take(2)
                names[0][0] + "" + names[1][0]
            }
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