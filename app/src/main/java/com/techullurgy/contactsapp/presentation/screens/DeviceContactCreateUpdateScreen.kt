package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.contactsapp.presentation.components.ContactTextField
import com.techullurgy.contactsapp.presentation.models.DeviceContactCreateUpdateScreenState
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenEvent
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceContactCreateUpdateScreen(
    viewModel: DeviceContactCreateUpdateScreenViewModel = koinViewModel(),
    onBack: () -> Unit,
    isEditRequest: Boolean = false
) {
    val state by viewModel.state.collectAsState()

    DeviceContactCreateUpdateScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onBack = onBack,
        isEditRequest = isEditRequest
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DeviceContactCreateUpdateScreen(
    state: DeviceContactCreateUpdateScreenState,
    onEvent: (DeviceContactCreateUpdateScreenEvent) -> Unit,
    onBack: () -> Unit,
    isEditRequest: Boolean
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
                        firstName = state.firstName,
                        onFirstNameChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnFirstNameChange(it)
                            )
                        },
                        lastName = state.lastName,
                        onLastNameChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnLastNameChange(it)
                            )
                        }
                    )
                }

                itemsIndexed(state.phones, key = { index, it -> "${it.hashCode()}-$index" }) { index, pair ->
                    PhoneField(
                        modifier = Modifier.animateItemPlacement(),
                        value = pair.second,
                        onValueChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnPhoneChange(value = it, type = pair.first, index = index)
                            )
                        },
                        type = pair.first,
                        onTypeChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnPhoneChange(value = pair.second, type = it, index = index)
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

                if(state.phones.size < 3) {
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

                itemsIndexed(state.emails, key = { index, it -> "$index-${it.hashCode()}-${it.first}" }) { index, pair ->
                    EmailField(
                        modifier = Modifier.animateItemPlacement(),
                        value = pair.second,
                        onValueChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEmailChange(value = it, type = pair.first, index = index)
                            )
                        },
                        type = pair.first,
                        onTypeChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEmailChange(value = pair.second, type = it, index = index)
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

                if(state.emails.size < 3) {
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

                itemsIndexed(state.events, key = { index, it -> "$index-${it.hashCode()}" }) { index, pair ->
                    EventField(
                        modifier = Modifier.animateItemPlacement(),
                        value = pair.second,
                        onValueChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEventChange(value = it, type = pair.first, index = index)
                            )
                        },
                        type = pair.first,
                        onTypeChange = {
                            onEvent(
                                DeviceContactCreateUpdateScreenEvent.OnEventChange(value = pair.second, type = it, index = index)
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

                if(state.events.size < 2) {
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
                                    DeviceContactCreateUpdateScreenEvent.OnUpdate(contactId = state.contactId) {
                                        onBack()
                                    }
                                )
                            },
                            enabled = state.creatable && state.editable
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
                            enabled = state.creatable
                        ) {
                            Text(text = "Create")
                        }
                    }
                    if(state.error.isNotBlank()) {
                        Text(text = state.error, color = Color.Red)
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
            ContactTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                placeholder = "First Name"
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Last Name", fontWeight = FontWeight.Bold)
            ContactTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                placeholder = "Last Name"
            )
        }
    }
}

@Composable
private fun PhoneField(
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
            ContactTextField(
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
            ContactTextField(
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
            ContactTextField(
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

@Composable
private fun TypeSelector(
    modifier: Modifier = Modifier,
    label: String,
    types: List<String>,
    selectedType: String,
    onSelectionChange: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)

        Row {
            types.forEach {
                Box(
                    modifier = Modifier
                        .clip(
                            MaterialTheme.shapes.medium
                        )
                        .run {
                            if (it == selectedType) {
                                background(MaterialTheme.colorScheme.primary)
                            } else this
                        }
                        .clickable {
                            onSelectionChange(it)
                        }
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it,
                        fontWeight = if(it == selectedType) FontWeight.Bold else null
                    )
                }
            }
        }
    }
}