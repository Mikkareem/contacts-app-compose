package com.techullurgy.contactsapp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.techullurgy.contactsapp.domain.model.RandomContact

@Composable
fun RandomContactListItem(
    modifier: Modifier = Modifier,
    contact: RandomContact,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick(contact.id) },
        shape = MaterialTheme.shapes.medium,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = contact.profileMedium,
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = contact.displayName, fontSize = 24.sp)
        }
    }
}