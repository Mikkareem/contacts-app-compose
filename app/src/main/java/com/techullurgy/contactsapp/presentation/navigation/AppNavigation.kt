package com.techullurgy.contactsapp.presentation.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techullurgy.contactsapp.presentation.screens.DeviceContactCreateUpdateScreen
import com.techullurgy.contactsapp.presentation.screens.DeviceContactDetailScreen
import com.techullurgy.contactsapp.presentation.screens.GlobalSearchScreen
import com.techullurgy.contactsapp.presentation.screens.RandomContactCreateUpdateScreen
import com.techullurgy.contactsapp.presentation.screens.RandomContactDetailScreen
import com.techullurgy.contactsapp.presentation.screens.TabsScreen
import com.techullurgy.contactsapp.presentation.screens.TwoPaneDeviceContactDetailScreen
import com.techullurgy.contactsapp.presentation.screens.TwoPaneRandomContactDetailScreen
import com.techullurgy.contactsapp.presentation.screens.TwoPaneScreen
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactCreateUpdateScreenViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Initial.name,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(
            route = Screen.Initial.name
        ) {
            InitialScreen(navController = navController)
        }

        composable(
            route = Screen.GlobalSearch.name
        ) {
            GlobalSearchScreenWithNavigation(navController = navController)
        }

        composable(
            route = Screen.RandomDetail.ROUTE,
            arguments = listOf(
                navArgument(
                    name = Screen.RandomDetail.ID,
                ) {
                    type = NavType.LongType
                }
            ),
        ) {
            val contactId = it.arguments?.getLong(Screen.RandomDetail.ID)

            val onContactEditClick: () -> Unit = {
                navController.navigate(
                    Screen.EditRandom(contactId!!).url
                ) {
                    launchSingleTop = true
                }
            }

            val windowSizeClass =
                calculateWindowSizeClass(activity = LocalContext.current as Activity)

            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    RandomContactDetailScreen(
                        contactId = contactId!!,
                        onContactEditClick = onContactEditClick,
                        onBack = { navController.popBackStack() }
                    )
                }

                else -> {
                    TwoPaneRandomContactDetailScreen(
                        contactId = contactId!!,
                        onContactEditClick = onContactEditClick,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(
            route = Screen.DeviceDetail.ROUTE,
            arguments = listOf(
                navArgument(
                    name = Screen.DeviceDetail.ID,
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            val contactId = it.arguments?.getString(Screen.DeviceDetail.ID)

            val onContactEditClick: () -> Unit = {
                navController.navigate(
                    Screen.EditDevice(contactId!!).url
                ) {
                    launchSingleTop = true
                }
            }

            val windowSizeClass =
                calculateWindowSizeClass(activity = LocalContext.current as Activity)

            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact -> {
                    DeviceContactDetailScreen(
                        contactId = contactId!!,
                        onContactEditClick = onContactEditClick,
                        onBack = { navController.popBackStack() }
                    )
                }

                else -> {
                    TwoPaneDeviceContactDetailScreen(
                        contactId = contactId!!,
                        onContactEditClick = onContactEditClick,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }

        composable(
            route = Screen.AddRandom.name
        ) {
            RandomContactCreateUpdateScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.AddDevice.name
        ) {
            DeviceContactCreateUpdateScreen(
                onBack = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.EditRandom.ROUTE,
            arguments = listOf(
                navArgument(
                    name = Screen.EditRandom.ID,
                ) {
                    type = NavType.LongType
                }
            )
        ) {
            val contactId = it.arguments?.getLong(Screen.EditRandom.ID)

            val viewModel: RandomContactCreateUpdateScreenViewModel = koinViewModel()

            LaunchedEffect(key1 = contactId) {
                contactId?.let {
                    viewModel.loadForUpdate(contactId)
                }
            }

            RandomContactCreateUpdateScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                isEditRequest = true
            )
        }

        composable(
            route = Screen.EditDevice.ROUTE,
            arguments = listOf(
                navArgument(
                    name = Screen.EditDevice.ID,
                ) {
                    type = NavType.StringType
                }
            )
        ) {
            val contactId = it.arguments?.getString(Screen.EditDevice.ID)

            val viewModel: DeviceContactCreateUpdateScreenViewModel = koinViewModel()

            LaunchedEffect(key1 = contactId) {
                contactId?.let {
                    viewModel.loadForUpdate(contactId)
                }
            }

            DeviceContactCreateUpdateScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                isEditRequest = true
            )
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
private fun AnimatedContentScope.InitialScreen(navController: NavHostController) {
    val onRandomContactClick: (id: Long) -> Unit = {
        navController.navigate(
            Screen.RandomDetail(it).url
        ){
            launchSingleTop = true
        }
    }

    val onDeviceContactClick: (id: String) -> Unit = {
        navController.navigate(
            Screen.DeviceDetail(it).url
        ){
            launchSingleTop = true
        }
    }

    val onAddDeviceContactClick: () -> Unit = {
        navController.navigate(
            Screen.AddDevice.name
        ) {
            launchSingleTop = true
        }
    }

    val onAddRandomContactClick: () -> Unit = {
        navController.navigate(
            Screen.AddRandom.name
        ) {
            launchSingleTop = true
        }
    }

    val onGlobalSearchClick: () -> Unit = {
        navController.navigate(
            Screen.GlobalSearch.name
        ) {
            launchSingleTop = true
        }
    }

    val windowSizeClass = calculateWindowSizeClass(activity = LocalContext.current as Activity)

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            TabsScreen(
                onRandomContactClick = onRandomContactClick,
                onDeviceContactClick = onDeviceContactClick,
                onAddDeviceClick = onAddDeviceContactClick,
                onAddRandomClick = onAddRandomContactClick,
                onGlobalSearchClick = onGlobalSearchClick
            )
        }

        else -> {
            TwoPaneScreen(
                onRandomContactClick = onRandomContactClick,
                onDeviceContactClick = onDeviceContactClick,
                onAddDeviceClick = onAddDeviceContactClick,
                onAddRandomClick = onAddRandomContactClick,
                onGlobalSearchClick = onGlobalSearchClick
            )
        }
    }

}

@Composable
private fun AnimatedContentScope.GlobalSearchScreenWithNavigation(
    navController: NavHostController
) {
    val onRandomContactClick: (id: Long) -> Unit = {
        navController.navigate(
            Screen.RandomDetail(it).url
        ){
            launchSingleTop = true
        }
    }

    val onDeviceContactClick: (id: String) -> Unit = {
        navController.navigate(
            Screen.DeviceDetail(it).url
        ){
            launchSingleTop = true
        }
    }

    GlobalSearchScreen(
        onRandomContactClick = onRandomContactClick,
        onDeviceContactClick = onDeviceContactClick
    )
}