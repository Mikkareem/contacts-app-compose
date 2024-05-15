package com.techullurgy.contactsapp.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.techullurgy.contactsapp.presentation.screens.DeviceContactCreateUpdateScreen
import com.techullurgy.contactsapp.presentation.screens.DeviceContactDetailScreen
import com.techullurgy.contactsapp.presentation.screens.GlobalSearchScreen
import com.techullurgy.contactsapp.presentation.screens.RandomContactDetailScreen
import com.techullurgy.contactsapp.presentation.screens.TabsScreen
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenViewModel
import org.koin.androidx.compose.koinViewModel

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

            RandomContactDetailScreen(contactId = contactId!!)
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

            DeviceContactDetailScreen(
                contactId = contactId!!,
                onContactEditClick = onContactEditClick
            )
        }

        composable(
            route = Screen.AddRandom.name
        ) {

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

    TabsScreen(
        onRandomContactClick = onRandomContactClick,
        onDeviceContactClick = onDeviceContactClick,
        onAddDeviceClick = onAddDeviceContactClick,
        onAddRandomClick = onAddRandomContactClick,
        onGlobalSearchClick = onGlobalSearchClick
    )
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