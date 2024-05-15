package com.techullurgy.contactsapp.presentation.viewmodels

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.techullurgy.contactsapp.presentation.screens.NameField
import com.techullurgy.contactsapp.presentation.screens.PhoneField
import org.koin.androidx.compose.koinViewModel

class DeviceCreateUpdateScreenVM2 : ViewModel() {
    var firstName by mutableStateOf("")
    var lastName by mutableStateOf("")
    var phones = mutableStateListOf(mutableStateOf("Home" to ""))

    fun onEvent(event: ScreenEvent) {
        when (event) {
            is ScreenEvent.OnFirstNameChange -> {
                firstName = event.value
            }

            is ScreenEvent.OnLastNameChange -> {
                lastName = event.value
            }

            is ScreenEvent.OnPhoneNoChange -> {
                phones[event.index].value = phones[event.index].value.first to event.value
            }

            is ScreenEvent.OnPhoneTypeChange -> {
                phones[event.index].value = event.value to phones[event.index].value.second
            }

            ScreenEvent.OnPhoneAdd -> {
                phones.add(mutableStateOf("Home" to "123"))
            }
        }
    }
}

sealed interface ScreenEvent {
    data class OnFirstNameChange(val value: String) : ScreenEvent
    data class OnLastNameChange(val value: String) : ScreenEvent
    data class OnPhoneNoChange(val index: Int, val value: String) : ScreenEvent
    data class OnPhoneTypeChange(val index: Int, val value: String) : ScreenEvent
    data object OnPhoneAdd : ScreenEvent
}

@Composable
fun Screen(
    viewModel: DeviceCreateUpdateScreenVM2 = koinViewModel()
) {
    LazyColumn {
        item {
            println("sssssssss")
            NameField(
                firstName = viewModel.firstName,
                onFirstNameChange = {
                    viewModel.onEvent(
                        ScreenEvent.OnFirstNameChange(it)
                    )
                },
                lastName = viewModel.lastName,
                onLastNameChange = {
                    viewModel.onEvent(
                        ScreenEvent.OnLastNameChange(it)
                    )
                }
            )
        }
        itemsIndexed(viewModel.phones) { index, item ->
            PhoneField(
                value = item.value.second,
                onValueChange = {
                    viewModel.onEvent(
                        ScreenEvent.OnPhoneNoChange(index, it)
                    )
                },
                type = item.value.first,
                onTypeChange = {
                    viewModel.onEvent(
                        ScreenEvent.OnPhoneTypeChange(index, it)
                    )
                },
                closeable = false,
                onClose = {}
            )
        }
        if (viewModel.phones.size < 3) {
            item {
                Button(onClick = { viewModel.onEvent(ScreenEvent.OnPhoneAdd) }) {
                    Text(text = "ADD")
                }
            }
        }
    }
}