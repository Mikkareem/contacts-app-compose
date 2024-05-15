package com.techullurgy.contactsapp.presentation.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun rememberPermissionState(
    permission: String,
    onGranted: () -> Unit,
    onDenied: () -> Unit
): PermissionState {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) {
        if(it) onGranted()
        else onDenied()
    }

    return remember {
        PermissionState(
            permission = permission,
            context = context,
            launcher = launcher,
            onGranted = onGranted
        )
    }
}


class PermissionState(
    private val permission: String,
    private val context: Context,
    private val launcher: ManagedActivityResultLauncher<String, Boolean>,
    private val onGranted: () -> Unit,
) {
    fun launch() {
        if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            onGranted()
        }

        launcher.launch(permission)
    }
}