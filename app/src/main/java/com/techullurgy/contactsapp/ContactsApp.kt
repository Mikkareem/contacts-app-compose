package com.techullurgy.contactsapp

import android.app.Application
import com.techullurgy.contactsapp.data.di.dataModule
import com.techullurgy.contactsapp.domain.di.domainModule
import com.techullurgy.contactsapp.presentation.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ContactsApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@ContactsApp)
            modules(dataModule, domainModule, presentationModule)
        }
    }
}