package com.techullurgy.contactsapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun TypeSelector(
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
                        fontWeight = if (it == selectedType) FontWeight.Bold else null
                    )
                }
            }
        }
    }
}