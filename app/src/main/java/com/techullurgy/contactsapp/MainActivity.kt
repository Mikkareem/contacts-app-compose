package com.techullurgy.contactsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.techullurgy.contactsapp.presentation.navigation.AppNavigation
import com.techullurgy.contactsapp.presentation.screens.TabsScreen
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