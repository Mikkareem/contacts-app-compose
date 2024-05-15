package com.techullurgy.contactsapp.data.di

import androidx.room.Room
import com.techullurgy.contactsapp.data.device.DeviceContactsManager
import com.techullurgy.contactsapp.data.local.LocalContactsManager
import com.techullurgy.contactsapp.data.local.room.daos.LocalContactsDao
import com.techullurgy.contactsapp.data.local.room.databases.ContactDatabase
import com.techullurgy.contactsapp.data.remote.RemoteContactsApi
import com.techullurgy.contactsapp.data.remote.RemoteContactsApiImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
    single<RemoteContactsApi> { RemoteContactsApiImpl(get()) }
    single { DeviceContactsManager(get()) }

    single<ContactDatabase> {
        Room.databaseBuilder(
            get(),
            ContactDatabase::class.java,
            "contacts_database_temp"
        ).build()
    }

    single<LocalContactsDao> {
        val database = get<ContactDatabase>()
        database.localContactsDao()
    }

    single { LocalContactsManager(get()) }
}