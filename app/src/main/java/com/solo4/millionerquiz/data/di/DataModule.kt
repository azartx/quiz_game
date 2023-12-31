package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.data.MediaManager
import com.solo4.millionerquiz.data.Storage
import com.solo4.millionerquiz.data.advert.AdvertController
import com.solo4.millionerquiz.data.auth.AuthManager
import com.solo4.millionerquiz.data.database.FirestoreDB
import com.solo4.millionerquiz.data.database.RemoteDBFetcher
import org.koin.dsl.module

val dataModule = module {
    single { AdvertController() }
    single { MediaManager(get()) }
    single { Storage(get()) }
    single { AuthManager(get()) }
    single { FirestoreDB(get(), get()) }
    single { RemoteDBFetcher(get(), get()) }
}
