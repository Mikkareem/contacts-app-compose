package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TwoPaneScreen(
    onRandomContactClick: (Long) -> Unit,
    onDeviceContactClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding()
                    .displayCutoutPadding()
            ) {
                Text(text = "Two Pane Screen")
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
}