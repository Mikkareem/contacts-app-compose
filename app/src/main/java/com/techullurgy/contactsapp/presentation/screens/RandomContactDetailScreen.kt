package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.techullurgy.contactsapp.domain.model.DeviceContactDetail
import com.techullurgy.contactsapp.domain.model.RandomContact
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactDetailViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RandomContactDetailScreen(
    contactId: Long,
    viewModel: RandomContactDetailViewModel = koinViewModel()
) {

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getContactDetail(contactId)
    }

    RandomContactDetailScreen(
        contact = state.randomContact,
        error = state.error
    )
}

@Composable
private fun RandomContactDetailScreen(
    contact: RandomContact?,
    error: String
) {
    if(error.isNotEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = error)
        }
        return
    }

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
                AsyncImage(
                    model = it.profileMedium,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = it.name, fontSize = 30.sp)
        }
    }
}