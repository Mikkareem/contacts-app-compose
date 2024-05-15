package com.techullurgy.contactsapp.presentation.di

import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactCreateUpdateScreenViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactDetailViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.DeviceContactsViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.GlobalSearchScreenViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactDetailViewModel
import com.techullurgy.contactsapp.presentation.viewmodels.RandomContactsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { RandomContactsViewModel(get()) }
    viewModel { DeviceContactsViewModel(get()) }
    viewModel { DeviceContactDetailViewModel(get()) }
    viewModel { RandomContactDetailViewModel(get()) }
    viewModel { DeviceContactCreateUpdateScreenViewModel(get()) }
    viewModel { GlobalSearchScreenViewModel(get()) }
}