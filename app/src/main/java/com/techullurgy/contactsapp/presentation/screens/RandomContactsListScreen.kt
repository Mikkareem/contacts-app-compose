package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techullurgy.contactsapp.domain.model.RandomContact
import com.techullurgy.contactsapp.presentation.components.RandomContactListItem
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun RandomContactsListScreen(
    viewModel: RandomContactsViewModel = koinViewModel(),
    onRandomContactClick: (Long) -> Unit
) {
    val state by viewModel.state.collectAsState()

    RandomContactsListScreen(
        contacts = state.contacts,
        pageLoading = state.pageLoading,
        pageError = state.pageError,
        error = state.error,
        loadMoreLoading = state.loadMoreLoading,
        onLoadMore = viewModel::onLoadMore,
        onRandomContactClick = onRandomContactClick
    )
}

@Composable
private fun RandomContactsListScreen(
    modifier: Modifier = Modifier,
    contacts: List<RandomContact>,
    pageLoading: Boolean,
    pageError: String,
    error: String,
    loadMoreLoading: Boolean,
    onLoadMore: () -> Unit,
    onRandomContactClick: (Long) -> Unit
) {
    if (pageLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (pageError.isNotEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Improve UI for this
            Column {
                Text(
                    text = pageError,
                    fontSize = 12.sp
                )
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Refresh")
                }
            }
        }
    } else if(contacts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Improve UI for this
            Column {
                Text(
                    text = "No Remote contacts found"
                )
                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Refresh")
                }
            }
        }
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(contacts) {
                RandomContactListItem(
                    contact = it,
                    onClick = onRandomContactClick
                )
            }
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (loadMoreLoading) {
                        CircularProgressIndicator()
                    } else if (error.isNotBlank()) {
                        Column {
                            Text(text = error)
                            Button(onClick = { /*TODO*/ }) {
                                Text(text = "Try Again")
                            }
                        }
                    } else {
                        TextButton(onClick = onLoadMore) {
                            Text(text = "Load More")
                        }
                    }
                }
            }
        }
    }
}