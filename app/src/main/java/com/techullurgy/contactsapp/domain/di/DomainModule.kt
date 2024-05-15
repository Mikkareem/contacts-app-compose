package com.techullurgy.contactsapp.domain.di

import com.techullurgy.contactsapp.domain.ContactsRepository
import org.koin.dsl.module

val domainModule = module {
    single { ContactsRepository(get(), get(), get()) }
}