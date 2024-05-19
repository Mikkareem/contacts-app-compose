package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.contactsapp.presentation.components.EmailContactTextField
import com.techullurgy.contactsapp.presentation.components.NameContactTextField
import com.techullurgy.contactsapp.presentation.components.PhoneContactTextField
import com.techullurgy.contactsapp.presentation.components.TypeSelector
import com.techullurgy.contactsapp.presentation.components.UrlContactTextField
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactCreateUpdateScreenEvent
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactCreateUpdateScreenViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RandomContactCreateUpdateScreen(
    viewModel: RandomContactCreateUpdateScreenViewModel = koinViewModel(),
    onBack: () -> Unit,
    isEditRequest: Boolean = false
) {
    val firstName = viewModel.firstName
    val lastName = viewModel.lastName
    val phone = viewModel.phone
    val cell = viewModel.cell
    val email = viewModel.email
    val gender = viewModel.gender
    val profileUrl = viewModel.profileUrl
    val error = viewModel.error

    RandomContactCreateUpdateScreen(
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        cell = cell,
        email = email,
        gender = gender,
        profileUrl = profileUrl,
        error = error,
        isEditRequest = isEditRequest,
        onBack = onBack,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun RandomContactCreateUpdateScreen(
    firstName: String,
    lastName: String,
    phone: String,
    cell: String,
    email: String,
    gender: String,
    profileUrl: String,
    error: String,
    isEditRequest: Boolean,
    onBack: () -> Unit,
    onEvent: (RandomContactCreateUpdateScreenEvent) -> Unit
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
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
                Text(
                    text = if (isEditRequest) "Update Random Contact" else "Create Random Contact",
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
                    Text(text = "First Name")
                    NameContactTextField(
                        value = firstName,
                        onValueChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnFirstNameChange(it)
                            )
                        },
                        placeholder = "First Name"
                    )
                }

                item {
                    Text(text = "Last Name")
                    NameContactTextField(
                        value = lastName,
                        onValueChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnLastNameChange(it)
                            )
                        },
                        placeholder = "Last Name"
                    )
                }

                item {
                    TypeSelector(
                        label = "Gender",
                        types = listOf("Male", "Female"),
                        selectedType = gender.replaceFirstChar { it.uppercase() },
                        onSelectionChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnGenderChange(it.lowercase())
                            )
                        }
                    )
                }

                item {
                    Text(text = "Phone")
                    PhoneContactTextField(
                        value = phone,
                        onValueChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnPhoneChange(it)
                            )
                        },
                        placeholder = "Phone"
                    )
                }

                item {
                    Text(text = "Cell")
                    PhoneContactTextField(
                        value = cell,
                        onValueChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnCellChange(it)
                            )
                        },
                        placeholder = "Cell"
                    )
                }

                item {
                    Text(text = "Email")
                    EmailContactTextField(
                        value = email,
                        onValueChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnEmailChange(it)
                            )
                        },
                        placeholder = "Email"
                    )
                }

                item {
                    Text(text = "Profile Url")
                    UrlContactTextField(
                        value = profileUrl,
                        onValueChange = {
                            onEvent(
                                RandomContactCreateUpdateScreenEvent.OnProfileUrlChange(it)
                            )
                        },
                        placeholder = "Profile Url"
                    )
                }

                item {
                    if (isEditRequest) {
                        Button(
                            onClick = {
                                onEvent(
                                    RandomContactCreateUpdateScreenEvent.OnUpdate {
                                        onBack()
                                    }
                                )
                            },
                            enabled = firstName.isNotBlank() && phone.isNotBlank()
                        ) {
                            Text(text = "Update")
                        }
                    } else {
                        Button(
                            onClick = {
                                onEvent(
                                    RandomContactCreateUpdateScreenEvent.OnCreate {
                                        onBack()
                                    }
                                )
                            },
                            enabled = firstName.isNotBlank() && phone.isNotBlank()
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