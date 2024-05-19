package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun TwoPaneScreen(
    onRandomContactClick: (Long) -> Unit,
    onDeviceContactClick: (String) -> Unit,
    onAddDeviceClick: () -> Unit,
    onAddRandomClick: () -> Unit,
    onGlobalSearchClick: () -> Unit
) {
    var showChooser by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .displayCutoutPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .clickable {
                                onGlobalSearchClick()
                            }
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Search Contacts",
                            color = LocalContentColor.current.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = LocalContentColor.current,
                onClick = { showChooser = true }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { pd ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(pd)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                RandomContactsListScreen(
                    onRandomContactClick = onRandomContactClick
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                DeviceContactsListScreen(
                    onDeviceContactClick = onDeviceContactClick
                )
            }
        }
    }

    if (showChooser) {
        Dialog(onDismissRequest = { showChooser = false }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddRandomClick()
                            showChooser = false
                        }
                        .padding(16.dp)
                ) {
                    Text(text = "Add Random Contact")
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAddDeviceClick()
                            showChooser = false
                        }
                        .padding(16.dp)
                ) {
                    Text(text = "Add Device Contact")
                }
            }
        }
    }
}