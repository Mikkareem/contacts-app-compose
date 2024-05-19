package com.techullurgy.contactsapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.techullurgy.contactsapp.presentation.navigation.AppNavigation
import com.techullurgy.contactsapp.ui.theme.ContactsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ContactsAppTheme {
                AppNavigation()
            }
        }
    }
}

fun Context.hasContactReadPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED
}

fun Context.hasContactWritePermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.WRITE_CONTACTS
    ) == PackageManager.PERMISSION_GRANTED
}

fun Activity.openAppSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", packageName, null)
    ).also(::startActivity)
}