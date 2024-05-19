package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techullurgy.contactsapp.domain.model.DeviceContact
import com.techullurgy.contactsapp.domain.model.RandomContact
import com.techullurgy.contactsapp.presentation.components.DeviceContactListItem
import com.techullurgy.contactsapp.presentation.components.RandomContactListItem
import com.techullurgy.contactsapp.presentation.models.GlobalSearchScreenState
import com.techullurgy.contactsapp.presentation.viewmodels.GlobalSearchScreenViewModel
import com.techullurgy.contactsapp.ui.theme.ContactsAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun GlobalSearchScreen(
    viewModel: GlobalSearchScreenViewModel = koinViewModel(),
    onRandomContactClick: (Long) -> Unit,
    onDeviceContactClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()

    GlobalSearchScreen(
        state = state,
        onQuery = viewModel::search,
        onRandomContactClick = onRandomContactClick,
        onDeviceContactClick = onDeviceContactClick
    )
}

@Composable
private fun GlobalSearchScreen(
    state: GlobalSearchScreenState,
    onQuery: (String) -> Unit,
    onRandomContactClick: (Long) -> Unit,
    onDeviceContactClick: (String) -> Unit
) {

    var query by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(key1 = query) {
        if(query.isNotEmpty()) {
            onQuery(query)
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .displayCutoutPadding()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Search Globally",
                            color = LocalContentColor.current.copy(alpha = 0.6f),
                            fontWeight = FontWeight.SemiBold
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.SemiBold),
                    shape = CircleShape,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if(query.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "You can search here both Random and Device Contacts",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.fillMaxWidth(.65f)
                    )
                }
            } else if(state.randoms.isEmpty() && state.devices.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No Contacts Found")
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if(state.randoms.isNotEmpty()) {
                        item {
                            Text(text = "Random", fontWeight = FontWeight.Bold)
                        }
                        items(state.randoms) { contact ->
                            RandomContactListItem(contact = contact, onClick = onRandomContactClick)
                        }
                    }
                    if(state.devices.isNotEmpty()) {
                        item {
                            Text(text = "Device", fontWeight = FontWeight.Bold)
                        }
                        items(state.devices) { contact ->
                            DeviceContactListItem(contact = contact, onClick = onDeviceContactClick)
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun GlobalSearchScreenPreview() {
    ContactsAppTheme {
        GlobalSearchScreen(
            state = GlobalSearchScreenState(
                randoms = listOf(
                    RandomContact(
                        id = 0,
                        firstName = "Rahul",
                        phone = "",
                        gender = "",
                        email = "",
                        profileMedium = "",
                        lastName = "",
                        cell = ""
                    ),
                    RandomContact(
                        id = 0,
                        firstName = "Rahul",
                        phone = "",
                        gender = "",
                        email = "",
                        profileMedium = "",
                        lastName = "",
                        cell = ""
                    ),
                    RandomContact(
                        id = 0,
                        firstName = "Rahul",
                        phone = "",
                        gender = "",
                        email = "",
                        profileMedium = "",
                        lastName = "",
                        cell = ""
                    ),
                ),
                devices = listOf(
                    DeviceContact("", "Irsath Kareem"),
                    DeviceContact("", "Irsath Kareem"),
                    DeviceContact("", "Irsath Kareem"),
                ),
            ),
            onQuery = {},
            onDeviceContactClick = {},
            onRandomContactClick = {}
        )
    }
}