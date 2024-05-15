package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                    }
                }
            }
        }
    }
}