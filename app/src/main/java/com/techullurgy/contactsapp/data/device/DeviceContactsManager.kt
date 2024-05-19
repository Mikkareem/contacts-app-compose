package com.techullurgy.contactsapp.data.device

import android.content.ContentProviderOperation
import android.content.Context
import android.provider.ContactsContract
import com.techullurgy.contactsapp.data.utils.ServiceResult
import com.techullurgy.contactsapp.domain.model.DeviceContact
import com.techullurgy.contactsapp.domain.model.DeviceContactDetail
import com.techullurgy.contactsapp.domain.model.DeviceContactRequest
import com.techullurgy.contactsapp.domain.model.EmailInformation
import com.techullurgy.contactsapp.domain.model.EmailType
import com.techullurgy.contactsapp.domain.model.EventInformation
import com.techullurgy.contactsapp.domain.model.EventType
import com.techullurgy.contactsapp.domain.model.PhoneInformation
import com.techullurgy.contactsapp.domain.model.PhoneType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeviceContactsManager(
    private val context: Context
) {
    suspend fun fetchAllContacts(): ServiceResult<List<DeviceContact>> = withContext(Dispatchers.IO) {

        val results = mutableListOf<DeviceContact>()

        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            ),
            null, null, null
        )?.use { contactCursor ->
            if(contactCursor.count < 1) {
                return@withContext ServiceResult.Failure("No device contacts found")
            }

            while(contactCursor.moveToNext()) {
                val contactId = contactCursor.getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val displayName = contactCursor.getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                results.add(
                    DeviceContact(
                        contactId = contactId,
                        displayName = displayName
                    )
                )
            }
            ServiceResult.Success(results)
        } ?: ServiceResult.Failure("Something went wrong while fetching the device contacts")
    }

    suspend fun getContactDetailFor(contactId: String): ServiceResult<DeviceContactDetail> = withContext(Dispatchers.IO) {

        val rawContactSelection = "${ContactsContract.RawContacts.CONTACT_ID} = $contactId"

        context.contentResolver.query(
            ContactsContract.RawContacts.CONTENT_URI,
            arrayOf(
                ContactsContract.RawContacts._ID
            ),
            rawContactSelection,
            null,
            null
        )?.use { rawContactsCursor ->
            if(rawContactsCursor.count < 1) {
                return@withContext ServiceResult.Failure("No device contacts found")
            }

            var deviceContactDetail = DeviceContactDetail(id = "", firstName = "", lastName = "", phones = emptyList(), emails = emptyList(), events = emptyList())

            if(rawContactsCursor.moveToNext()) {
                val rawContactId = rawContactsCursor.getString(rawContactsCursor.getColumnIndexOrThrow(ContactsContract.RawContacts._ID))

                deviceContactDetail = deviceContactDetail.copy(
                    id = rawContactId
                )

                val dataSelection = "${ContactsContract.Data.RAW_CONTACT_ID} = $rawContactId"

                context.contentResolver.query(
                    ContactsContract.Data.CONTENT_URI,
                    arrayOf(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.Data.DATA1,
                        ContactsContract.Data.DATA2,
                        ContactsContract.Data.DATA3,
                    ),
                    dataSelection,
                    null,
                    null
                )?.use { dataCursor ->
                    if(dataCursor.count < 1) {
                        return@withContext ServiceResult.Failure("No device contacts found")
                    }

                    while(dataCursor.moveToNext()) {
                        val mimetype = dataCursor.getString(dataCursor.getColumnIndexOrThrow(ContactsContract.Data.MIMETYPE))
                        val data1 = dataCursor.getString(dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DATA1))
                        val data2 = dataCursor.getString(dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DATA2))
                        val data3 = dataCursor.getString(dataCursor.getColumnIndexOrThrow(ContactsContract.Data.DATA3))

                        when(mimetype) {
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE -> {
                                deviceContactDetail = deviceContactDetail.copy(
                                    firstName = data2,
                                    lastName = data3
                                )
                            }
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE -> {
                                deviceContactDetail = when(data2.toInt()) {
                                    ContactsContract.CommonDataKinds.Email.TYPE_HOME -> {
                                        deviceContactDetail.copy(
                                            emails = deviceContactDetail.emails.toMutableList().apply {
                                                add(
                                                    EmailInformation.HomeEmail(email = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    ContactsContract.CommonDataKinds.Email.TYPE_WORK -> {
                                        deviceContactDetail.copy(
                                            emails = deviceContactDetail.emails.toMutableList().apply {
                                                add(
                                                    EmailInformation.WorkEmail(email = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    ContactsContract.CommonDataKinds.Email.TYPE_MOBILE -> {
                                        deviceContactDetail.copy(
                                            emails = deviceContactDetail.emails.toMutableList().apply {
                                                add(
                                                    EmailInformation.MobileEmail(email = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    else -> deviceContactDetail
                                }
                            }
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE -> {
                                deviceContactDetail = when(data2.toInt()) {
                                    ContactsContract.CommonDataKinds.Phone.TYPE_HOME -> {
                                        deviceContactDetail.copy(
                                            phones = deviceContactDetail.phones.toMutableList().apply {
                                                add(
                                                    PhoneInformation.HomePhone(phoneNo = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    ContactsContract.CommonDataKinds.Phone.TYPE_WORK -> {
                                        deviceContactDetail.copy(
                                            phones = deviceContactDetail.phones.toMutableList().apply {
                                                add(
                                                    PhoneInformation.WorkPhone(phoneNo = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE -> {
                                        deviceContactDetail.copy(
                                            phones = deviceContactDetail.phones.toMutableList().apply {
                                                add(
                                                    PhoneInformation.MobilePhone(phoneNo = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    else -> deviceContactDetail
                                }
                            }
                            ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE -> {
                                deviceContactDetail = when(data2.toInt()) {
                                    ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY -> {
                                        deviceContactDetail.copy(
                                            events = deviceContactDetail.events.toMutableList().apply {
                                                add(
                                                    EventInformation.BirthdayEvent(eventDate = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY -> {
                                        deviceContactDetail.copy(
                                            events = deviceContactDetail.events.toMutableList().apply {
                                                add(
                                                    EventInformation.AnniversaryEvent(eventDate = data1)
                                                )
                                            }.toList()
                                        )
                                    }
                                    else -> deviceContactDetail
                                }
                            }
                        }
                    }
                } ?: ServiceResult.Failure("Something went wrong while fetching the device contacts")
            }
            ServiceResult.Success(deviceContactDetail)
        } ?: ServiceResult.Failure("Something went wrong while fetching the device contacts")
    }

    suspend fun addNewContact(request: DeviceContactRequest): Unit = withContext(Dispatchers.IO) {
        val ops = arrayListOf<ContentProviderOperation>()

        var op = ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)

        ops.add(op.build())

        op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, request.firstName + " " + request.lastName)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, request.firstName)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, request.lastName)

        ops.add(op.build())

        if(request.phone.isNotEmpty()) {
            request.phone.map { detail ->
                val phoneType = when(detail.first) {
                    PhoneType.HOME.value -> ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                    PhoneType.MOBILE.value -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    PhoneType.WORK.value -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                    else -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
                }

                op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, detail.second) // Phone no
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phoneType) // Phone Type

                op.withYieldAllowed(true)

                ops.add(op.build())
            }
        }

        if(request.email.isNotEmpty()) {
            request.email.map { detail ->
                val emailType = when(detail.first) {
                    EmailType.HOME.value -> ContactsContract.CommonDataKinds.Email.TYPE_HOME
                    EmailType.MOBILE.value -> ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
                    EmailType.WORK.value -> ContactsContract.CommonDataKinds.Email.TYPE_WORK
                    else -> ContactsContract.CommonDataKinds.Email.TYPE_OTHER
                }

                op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, detail.second) // Email Id
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, emailType) // Email Type

                op.withYieldAllowed(true)

                ops.add(op.build())
            }
        }

        if(request.event.isNotEmpty()) {
            request.event.map { detail ->
                val eventType = when(detail.first) {
                    EventType.BIRTHDAY.value -> ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
                    EventType.ANNIVERSARY.value -> ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY
                    else -> ContactsContract.CommonDataKinds.Event.TYPE_OTHER
                }

                op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Event.DATA, detail.second) // Event date
                    .withValue(ContactsContract.CommonDataKinds.Event.TYPE, eventType) // Event type

                op.withYieldAllowed(true)

                ops.add(op.build())
            }
        }

        context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

    suspend fun editContact(contactId: String, request: DeviceContactRequest): Unit = withContext(Dispatchers.IO) {

        val phoneTypes = mutableListOf(ContactsContract.CommonDataKinds.Phone.TYPE_HOME, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE, ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
        val emailTypes = mutableListOf(ContactsContract.CommonDataKinds.Email.TYPE_HOME, ContactsContract.CommonDataKinds.Email.TYPE_MOBILE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
        val eventTypes = mutableListOf(ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY, ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY)

        val ops = arrayListOf<ContentProviderOperation>()

        var op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
            .withSelection(
                "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ?",
                arrayOf(contactId, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            )
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, request.firstName + " " + request.lastName)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, request.firstName)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, request.lastName)

        ops.add(op.build())

        if(request.phone.isNotEmpty()) {
            request.phone.map { detail ->
                val phoneType = when(detail.first) {
                    PhoneType.HOME.value -> ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                    PhoneType.MOBILE.value -> ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    PhoneType.WORK.value -> ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                    else -> ContactsContract.CommonDataKinds.Phone.TYPE_OTHER
                }

                if(
                    isAvailable(
                        contactId,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        phoneType
                    )
                ) {
                    op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Phone.TYPE} = ?",
                            arrayOf(contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, phoneType.toString())
                        )
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, detail.second) // Phone no
                } else {
                    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, contactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, detail.second) // Phone no
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phoneType) // Phone Type
                }
                op.withYieldAllowed(true)

                ops.add(op.build())

                phoneTypes.remove(phoneType)
            }
        }

        if(request.email.isNotEmpty()) {
            request.email.map { detail ->
                val emailType = when(detail.first) {
                    EmailType.HOME.value -> ContactsContract.CommonDataKinds.Email.TYPE_HOME
                    EmailType.MOBILE.value -> ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
                    EmailType.WORK.value -> ContactsContract.CommonDataKinds.Email.TYPE_WORK
                    else -> ContactsContract.CommonDataKinds.Email.TYPE_OTHER
                }

                if(
                    isAvailable(
                        contactId,
                        ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        emailType
                    )
                ) {
                    op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Email.TYPE} = ?",
                            arrayOf(contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, emailType.toString())
                        )
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, detail.second) // Email Id
                } else {
                    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, contactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, detail.second) // Email Id
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, emailType) // Email Type
                }

                op.withYieldAllowed(true)

                ops.add(op.build())

                emailTypes.remove(emailType)
            }
        }

        if(request.event.isNotEmpty()) {
            request.event.map { detail ->
                val eventType = when(detail.first) {
                    EventType.BIRTHDAY.value -> ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
                    EventType.ANNIVERSARY.value -> ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY
                    else -> ContactsContract.CommonDataKinds.Event.TYPE_OTHER
                }

                if(
                    isAvailable(
                        contactId,
                        ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE,
                        ContactsContract.CommonDataKinds.Event.TYPE,
                        eventType
                    )
                ) {
                    op = ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                        .withSelection(
                            "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Event.TYPE} = ?",
                            arrayOf(contactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, eventType.toString())
                        )
                        .withValue(ContactsContract.CommonDataKinds.Event.DATA, detail.second) // Event Date
                } else {
                    op = ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValue(ContactsContract.Data.RAW_CONTACT_ID, contactId)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Event.DATA, detail.second) // Event Date
                        .withValue(ContactsContract.CommonDataKinds.Event.TYPE, eventType) // Event Type
                }

                op.withYieldAllowed(true)

                ops.add(op.build())

                eventTypes.remove(eventType)
            }
        }

        phoneTypes.forEach {
            op = deletePhoneRequest(contactId, it)
            op.withYieldAllowed(true)
            ops.add(op.build())
        }

        emailTypes.forEach {
            op = deleteEmailRequest(contactId, it)
            op.withYieldAllowed(true)
            ops.add(op.build())
        }

        eventTypes.forEach {
            op = deleteEventRequest(contactId, it)
            op.withYieldAllowed(true)
            ops.add(op.build())
        }

        context.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
    }

    private fun isAvailable(contactId: String, mimeType: String, typeColumn: String, type: Int): Boolean {
        context.contentResolver.query(
            ContactsContract.Data.CONTENT_URI,
            null,
            "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND $typeColumn = ?",
            arrayOf(contactId, mimeType, type.toString()),
            null
        )?.use {
            return it.count > 0
        }

        return false
    }

    private fun deletePhoneRequest(contactId: String, type: Int): ContentProviderOperation.Builder {
        return ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
            .withSelection(
                "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Phone.TYPE} = ?",
                arrayOf(contactId, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE, type.toString())
            )
    }

    private fun deleteEmailRequest(contactId: String, type: Int): ContentProviderOperation.Builder {
        return ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
            .withSelection(
                "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Email.TYPE} = ?",
                arrayOf(contactId, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, type.toString())
            )
    }

    private fun deleteEventRequest(contactId: String, type: Int): ContentProviderOperation.Builder {
        return ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
            .withSelection(
                "${ContactsContract.Data.RAW_CONTACT_ID} = ? AND ${ContactsContract.Data.MIMETYPE} = ? AND ${ContactsContract.CommonDataKinds.Event.TYPE} = ?",
                arrayOf(contactId, ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, type.toString())
            )
    }

    suspend fun getSearchResultsFor(query: String): List<DeviceContact> = withContext(Dispatchers.IO) {
        val results = mutableListOf<DeviceContact>()

        context.contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            arrayOf(
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
            ),
            "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ?",
            arrayOf("%$query%"),
            null
        )?.use { contactCursor ->
            if(contactCursor.count < 1) {
                return@withContext results
            }

            while(contactCursor.moveToNext()) {
                val contactId = contactCursor.getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val displayName = contactCursor.getString(contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY))

                results.add(
                    DeviceContact(
                        contactId = contactId,
                        displayName = displayName
                    )
                )
            }
            results
        } ?: results
    }
}