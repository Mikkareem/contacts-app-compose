package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.contactsapp.domain.model.DeviceContactDetail
import com.techullurgy.contactsapp.domain.model.EmailInformation
import com.techullurgy.contactsapp.domain.model.EmailType
import com.techullurgy.contactsapp.domain.model.EventInformation
import com.techullurgy.contactsapp.domain.model.EventType
import com.techullurgy.contactsapp.domain.model.PhoneInformation
import com.techullurgy.contactsapp.domain.model.PhoneType
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DeviceContactDetailScreen(
    contactId: String,
    viewModel: DeviceContactDetailViewModel = koinViewModel(),
    onBack: () -> Unit,
    onContactEditClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getContactDetail(contactId)
    }

    DeviceContactDetailScreen(
        contact = state.contactDetail,
        error = state.error,
        onBack = onBack,
        onContactEditClick = onContactEditClick
    )
}

@Composable
private fun DeviceContactDetailScreen(
    contact: DeviceContactDetail?,
    error: String,
    onBack: () -> Unit,
    onContactEditClick: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
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
                    text = "Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onContactEditClick) {
                    Icon(imageVector = Icons.Filled.Create, contentDescription = null)
                }
            }
        }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(it)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if(error.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error)
                }
            } else {
                contact?.let {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Green),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = it.initials, fontSize = 42.sp)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = it.firstName, fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(48.dp))
                        if (it.phones.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    it.phones.forEachIndexed { index, item ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Call,
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column {
                                                when (item) {
                                                    is PhoneInformation.HomePhone -> {
                                                        Text(text = item.phoneNo, fontSize = 24.sp)
                                                        Text(
                                                            text = PhoneType.HOME.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }

                                                    is PhoneInformation.MobilePhone -> {
                                                        Text(text = item.phoneNo, fontSize = 24.sp)
                                                        Text(
                                                            text = PhoneType.MOBILE.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }

                                                    is PhoneInformation.WorkPhone -> {
                                                        Text(text = item.phoneNo, fontSize = 24.sp)
                                                        Text(
                                                            text = PhoneType.WORK.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        if (index != it.phones.lastIndex) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (it.emails.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    it.emails.forEachIndexed { index, item ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.Email,
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column {
                                                when (item) {
                                                    is EmailInformation.HomeEmail -> {
                                                        Text(text = item.email, fontSize = 24.sp)
                                                        Text(
                                                            text = EmailType.HOME.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }

                                                    is EmailInformation.MobileEmail -> {
                                                        Text(text = item.email, fontSize = 24.sp)
                                                        Text(
                                                            text = EmailType.MOBILE.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }

                                                    is EmailInformation.WorkEmail -> {
                                                        Text(text = item.email, fontSize = 24.sp)
                                                        Text(
                                                            text = EmailType.WORK.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        if (index != it.emails.lastIndex) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        if (it.events.isNotEmpty()) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    it.events.forEachIndexed { index, item ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.DateRange,
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(16.dp))
                                            Column {
                                                when (item) {
                                                    is EventInformation.BirthdayEvent -> {
                                                        Text(
                                                            text = item.eventDate,
                                                            fontSize = 24.sp
                                                        )
                                                        Text(
                                                            text = EventType.BIRTHDAY.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }

                                                    is EventInformation.AnniversaryEvent -> {
                                                        Text(
                                                            text = item.eventDate,
                                                            fontSize = 24.sp
                                                        )
                                                        Text(
                                                            text = EventType.ANNIVERSARY.value,
                                                            color = LocalContentColor.current.copy(
                                                                alpha = 0.6f
                                                            )
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                        if (index != it.events.lastIndex) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}