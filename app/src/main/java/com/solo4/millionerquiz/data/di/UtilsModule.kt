package com.solo4.millionerquiz.data.di

import android.content.Context.MODE_PRIVATE
import com.google.gson.Gson
import com.solo4.millionerquiz.data.database.deserializers.FirebaseDBDataDeserializer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val utilsModule = module {
    single { Gson() }
    factory { FirebaseDBDataDeserializer(get()) }
    factory { androidContext().getSharedPreferences("APP_PREFS", MODE_PRIVATE) }
}
