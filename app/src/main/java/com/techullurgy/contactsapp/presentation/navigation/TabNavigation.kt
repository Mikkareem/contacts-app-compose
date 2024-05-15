package com.techullurgy.contactsapp.presentation.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.techullurgy.contactsapp.presentation.screens.DeviceContactsListScreen
import com.techullurgy.contactsapp.presentation.screens.RandomContactsListScreen

@Composable
fun TabNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onRandomContactClick: (Long) -> Unit,
    onDeviceContactClick: (String) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.RandomList.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        composable(Screen.RandomList.name) {
            RandomContactsListScreen(
                onRandomContactClick = onRandomContactClick
            )
        }
        composable(Screen.DeviceList.name) {
            DeviceContactsListScreen(
                onDeviceContactClick = onDeviceContactClick
            )
        }
    }
}