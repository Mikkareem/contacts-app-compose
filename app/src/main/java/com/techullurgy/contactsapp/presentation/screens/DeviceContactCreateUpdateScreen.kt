package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.contactsapp.presentation.components.DateContactTextField
import com.techullurgy.contactsapp.presentation.components.EmailContactTextField
import com.techullurgy.contactsapp.presentation.components.NameContactTextField
import com.techullurgy.contactsapp.presentation.components.PhoneContactTextField
import com.techullurgy.contactsapp.presentation.components.TypeSelector
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenEvent
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceContactCreateUpdateScreen(
    viewModel: DeviceContactCreateUpdateScreenViewModel = koinViewModel(),
    onBack: () -> Unit,
    isEditRequest: Boolean = false
) {
    val firstName = viewModel.firstName
    val lastName = viewModel.lastName
    val phones = viewModel.phones
    val emails = viewModel.emails
    val events = viewModel.events
    val error = viewModel.error

    DeviceContactCreateUpdateScreen(
        onEvent = viewModel::onEvent,
        onBack = onBack,
        isEditRequest = isEditRequest,
        firstName = firstName,
        lastName = lastName,
        phones = phones,
        emails = emails,
        events = events,
        error = error
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceContactCreateUpdateScreen(
    onEvent: (DeviceContactCreateUpdateScreenEvent) -> Unit,
    onBack: () -> Unit,
    isEditRequest: Boolean,
    firstName: String,
    lastName: String,
    phones: SnapshotStateList<MutableState<Pair<String, String>>>,
    emails: SnapshotStateList<MutableState<Pair<String, String>>>,
    events: SnapshotStateList<MutableState<Pair<String, String>>>,
    error: String
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .displayCutoutPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = if(isEditRequest) "Update Device Contact" else "Create Device Contact",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { pd ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pd)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        awaitFirstDown(pass = PointerEventPass.Final)
                        focusManager.clearFocus()
                    }
                }
        ) {
            LazyColumn(
                modifier = Modifier
                    .imePadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    NameField(
                        firstName = firstName,
                        onFirstNameChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnFirstNameChange(it)
                            )
                        },
                        lastName = lastName,
                        onLastNameChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnLastNameChange(it)
                            )
                        }
                    )
                }

                itemsIndexed(
                    phones,
                    key = { index, it -> "${it.hashCode()}-$index" }) { index, pair ->
                    PhoneField(
                        modifier = Modifier.animateItemPlacement(),
                        value = pair.value.second,
                        onValueChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnPhoneChange(
                                    value = it,
                                    index = index
                                )
                            )
                        },
                        type = pair.value.first,
                        onTypeChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnPhoneTypeChange(
                                    value = it,
                                    index = index
                                )
                            )
                        },
                        closeable = index != 0,
                        onClose = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnClosePhone(index)
                            )
                        }
                    )
                }

                if (phones.size < 3) {
                    item(key = "Add Phone") {
                        TextButton(
                            onClick = {
                                onEvent(
                                    DeviceContactCreateUpdateScreenEvent.OnAddPhone
                                )
                            }
                        ) {
                            Text(text = "Add Phone", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                itemsIndexed(
                    emails,
                    key = { index, it -> "$index-${it.hashCode()}-${it}" }) { index, pair ->
                    EmailField(
                        modifier = Modifier.animateItemPlacement(),
                        value = pair.value.second,
                        onValueChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEmailChange(
                                    value = it,
                                    index = index
                                )
                            )
                        },
                        type = pair.value.first,
                        onTypeChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEmailTypeChange(
                                    value = it,
                                    index = index
                                )
                            )
                        },
                        closeable = index != 0,
                        onClose = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnCloseEmail(index)
                            )
                        }
                    )
                }

                if (emails.size < 3) {
                    item(key = "Add Email") {
                        TextButton(
                            onClick = {
                                onEvent(
                                    DeviceContactCreateUpdateScreenEvent.OnAddEmail
                                )
                            }
                        ) {
                            Text(text = "Add Email", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                itemsIndexed(
                    events,
                    key = { index, it -> "$index-${it.hashCode()}" }) { index, pair ->
                    EventField(
                        modifier = Modifier.animateItemPlacement(),
                        value = pair.value.second,
                        onValueChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEventChange(
                                    value = it,
                                    index = index
                                )
                            )
                        },
                        type = pair.value.first,
                        onTypeChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEventTypeChange(
                                    value = it,
                                    index = index
                                )
                            )
                        },
                        closeable = index != 0,
                        onClose = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnCloseEvent(index)
                            )
                        }
                    )
                }

                if (events.size < 2) {
                    item(key = "Add Event") {
                        TextButton(
                            onClick = {
                                onEvent(
                                    DeviceContactCreateUpdateScreenEvent.OnAddEvent
                                )
                            }
                        ) {
                            Text(text = "Add Event", fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                item {
                    if(isEditRequest) {
                        Button(
                            onClick = {
                                onEvent(
                                    DeviceContactCreateUpdateScreenEvent.OnUpdate {
                                        onBack()
                                    }
                                )
                            },
                            enabled = firstName.isNotBlank() && phones.any { it.value.second.isNotBlank() }
                        ) {
                            Text(text = "Update")
                        }
                    } else {
                        Button(
                            onClick = {
                                onEvent(
                                    DeviceContactCreateUpdateScreenEvent.OnCreate {
                                        onBack()
                                    }
                                )
                            },
                            enabled = firstName.isNotBlank() && phones.any { it.value.second.isNotBlank() }
                        ) {
                            Text(text = "Create")
                        }
                    }
                    if (error.isNotBlank()) {
                        Text(text = error, color = Color.Red)
                    }
                }
            }
        }
    }
}

@Composable
fun NameField(
    modifier: Modifier = Modifier,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "First Name", fontWeight = FontWeight.Bold)
            NameContactTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                placeholder = "First Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Last Name", fontWeight = FontWeight.Bold)
            NameContactTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                placeholder = "Last Name"
            )
        }
    }
}

@Stable
@Composable
fun PhoneField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    closeable: Boolean,
    onClose: () -> Unit
) {
    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if(closeable) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onClose() }
                )
            }
            Text(text = "Phone", fontWeight = FontWeight.Bold)
            PhoneContactTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = "Phone"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TypeSelector(
                label = "Phone Type",
                types = listOf("Home", "Work", "Mobile"),
                selectedType = type,
                onSelectionChange = onTypeChange
            )
        }
    }
}

@Composable
private fun EmailField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    closeable: Boolean,
    onClose: () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if(closeable) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onClose() }
                )
            }
            Text(text = "Email", fontWeight = FontWeight.Bold)
            EmailContactTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = "Email"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TypeSelector(
                label = "Email Type",
                types = listOf("Home", "Work", "Mobile"),
                selectedType = type,
                onSelectionChange = onTypeChange
            )
        }
    }
}

@Composable
private fun EventField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    type: String,
    onTypeChange: (String) -> Unit,
    closeable: Boolean,
    onClose: () -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if(closeable) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { onClose() }
                )
            }
            Text(text = "Event", fontWeight = FontWeight.Bold)
            DateContactTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = "YYYY/MM/DD"
            )
            Spacer(modifier = Modifier.height(16.dp))
            TypeSelector(
                label = "Event Type",
                types = listOf("Birthday", "Anniversary"),
                selectedType = type,
                onSelectionChange = onTypeChange
            )
        }
    }
}