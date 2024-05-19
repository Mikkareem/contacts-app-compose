package com.techullurgy.contactsapp.presentation.screens

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techullurgy.contactsapp.domain.model.DeviceContact
import com.techullurgy.contactsapp.hasContactReadPermission
import com.techullurgy.contactsapp.presentation.components.DeviceContactListItem
import com.techullurgy.contactsapp.presentation.utils.rememberPermissionState
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceContactsListScreen(
    onDeviceContactClick: (String) -> Unit
) {
    val context = LocalContext.current

    var isContactPermissionAvailable by remember {
        mutableStateOf(context.hasContactReadPermission())
    }

    val givePermissionLauncher = rememberPermissionState(
        permission = Manifest.permission.READ_CONTACTS,
        onGranted = { isContactPermissionAvailable = true },
        onDenied = { isContactPermissionAvailable = false }
    )

    if (isContactPermissionAvailable) {
        DeviceContactsListScreen2(
            onDeviceContactClick = onDeviceContactClick
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Please give Read contact Permission to show here the device contacts",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(0.75f)
                )
                Button(onClick = { givePermissionLauncher.launch() }) {
                    Text(text = "Give Permission now")
                }
            }
        }
    }
}


@Composable
private fun DeviceContactsListScreen2(
    viewModel: DeviceContactsViewModel = koinViewModel(),
    onDeviceContactClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    DeviceContactsListScreen(
        contacts = state.contacts,
        error = state.error,
        onDeviceContactClick = onDeviceContactClick
    )
}

@Composable
private fun DeviceContactsListScreen(
    modifier: Modifier = Modifier,
    contacts: List<DeviceContact>,
    error: String,
    onDeviceContactClick: (String) -> Unit
) {
    if(error.isNotEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Improve UI for this
            Text(
                text = error
            )
        }
    } else if(contacts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Improve UI for this
            Text(
                text = "No Device contacts found"
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(contacts) {
                DeviceContactListItem(contact = it, onClick = onDeviceContactClick)
            }
        }
    }
}