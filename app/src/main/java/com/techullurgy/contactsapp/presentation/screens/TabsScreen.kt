package com.techullurgy.contactsapp.presentation.screens

import android.Manifest
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.techullurgy.contactsapp.presentation.navigation.TabNavigation
import com.techullurgy.contactsapp.presentation.navigation.Screen
import com.techullurgy.contactsapp.presentation.utils.rememberPermissionState
import com.techullurgy.contactsapp.ui.theme.ContactsAppTheme

@Composable
fun TabsScreen(
    modifier: Modifier = Modifier,
    onRandomContactClick: (Long) -> Unit,
    onDeviceContactClick: (String) -> Unit,
    onAddRandomClick: () -> Unit,
    onAddDeviceClick: () -> Unit,
    onGlobalSearchClick: () -> Unit
) {
    val navController = rememberNavController()

    val backstackState by navController.currentBackStackEntryAsState()

    val tabs = listOf("Random", "Device")

    val selectedIndex = when(backstackState?.destination?.route) {
        Screen.RandomList.name -> 0
        Screen.DeviceList.name -> 1
        else -> -1
    }

    val deviceTabClickLauncher = rememberPermissionState(
        permission = Manifest.permission.READ_CONTACTS,
        onGranted = {
            navController.navigate(Screen.DeviceList.name) {
                launchSingleTop = true
                popUpTo(navController.graph.findStartDestination().route!!) {
                    inclusive = false
                }
            }
        },
        onDenied = {
            // TODO: Improve UI
            Log.d("_TAG_", "Read Contacts Permission Denied")
        }
    )

    val deviceAddClickLauncher = rememberPermissionState(
        permission = Manifest.permission.WRITE_CONTACTS,
        onGranted = {
            onAddDeviceClick()
        },
        onDenied = {
            // TODO: Improve UI
            Log.d("_TAG_", "Write Contacts Permission Denied")
        }
    )

    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(false, PointerEventPass.Final)
                    focusManager.clearFocus()
                }
            },
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
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp, end = 16.dp)
                    ,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        currentContext = if(selectedIndex != -1) tabs[selectedIndex] else ""
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.Green)
                            .clickable {
                                onGlobalSearchClick()
                            }
                            .padding(8.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Tabs(
                    tabs = tabs,
                    selectedIndex = selectedIndex,
                    onTabClick = {
                        when(it) {
                            0 -> {
                                navController.navigate(Screen.RandomList.name) {
                                    launchSingleTop = true
                                    popUpTo(navController.graph.findStartDestination().route!!) {
                                        inclusive = false
                                    }
                                }
                            }
                            1 -> {
                                deviceTabClickLauncher.launch()
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = LocalContentColor.current,
                onClick = {
                    when(selectedIndex) {
                        0 -> {
                            onAddRandomClick()
                        }
                        1 -> {
                            deviceAddClickLauncher.launch()
                        }
                    }
                }
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { pd ->
        TabNavigation(
            modifier = modifier.padding(pd),
            navController = navController,
            onRandomContactClick = onRandomContactClick,
            onDeviceContactClick = onDeviceContactClick
        )
    }
}

@Composable
private fun Tabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    selectedIndex: Int,
    onTabClick: (Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        tabs.forEachIndexed { index, tab ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        if (selectedIndex != index) {
                            onTabClick(index)
                        }
                    },
            ) {
                Text(
                    text = tab,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            if (index == selectedIndex) {
                                // TODO: Draw indicator
                                drawLine(
                                    color = Color.Blue,
                                    start = Offset(x = 0f, y = size.height),
                                    end = Offset(x = size.width, y = size.height),
                                    strokeWidth = 3.dp.toPx()
                                )
                            }
                        }
                        .padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun RowScope.SearchBar(
    modifier: Modifier = Modifier,
    currentContext: String
) {
    // TODO: Search Bar
    Box(
        modifier = modifier.weight(1f)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = {
                Text(text = "Search $currentContext Contacts")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@Preview
@Composable
private fun TabsScreenPreview() {
    ContactsAppTheme {
        TabsScreen(
            onRandomContactClick = {},
            onDeviceContactClick = {},
            onAddRandomClick = { /*TODO*/ },
            onAddDeviceClick = { /*TODO*/ },
            onGlobalSearchClick = {}
        )
    }
}