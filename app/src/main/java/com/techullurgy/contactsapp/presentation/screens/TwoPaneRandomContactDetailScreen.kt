package com.techullurgy.contactsapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.techullurgy.contactsapp.domain.model.RandomContact
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactDetailViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TwoPaneRandomContactDetailScreen(
    contactId: Long,
    viewModel: RandomContactDetailViewModel = koinViewModel(),
    onBack: () -> Unit,
    onContactEditClick: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.getContactDetail(contactId)
    }

    TwoPaneRandomContactDetailScreen(
        contact = state.randomContact,
        error = state.error,
        onContactEditClick = onContactEditClick,
        onBack = onBack
    )
}

@Composable
private fun TwoPaneRandomContactDetailScreen(
    contact: RandomContact?,
    error: String,
    onBack: () -> Unit,
    onContactEditClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
    ) { pd ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(pd)
        ) {
            if (error.isNotEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = error)
                }
            } else {
                contact?.let {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var photoError by rememberSaveable {
                            mutableStateOf(false)
                        }
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.Green),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!photoError) {
                                AsyncImage(
                                    model = it.profileMedium,
                                    contentDescription = null,
                                    onError = {
                                        photoError = true
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = it.initials, fontSize = 38.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(text = it.displayName, fontSize = 30.sp)
                    }

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append("First Name: ")
                                }
                                append(it.firstName)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append("Last Name: ")
                                }
                                append(it.lastName)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append("Phone: ")
                                }
                                append(it.phone)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append("Cell: ")
                                }
                                append(it.cell)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append("Email: ")
                                }
                                append(it.email)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append("Gender: ")
                                }
                                append(it.gender)
                            }
                        )
                    }
                }
            }
        }
    }
}