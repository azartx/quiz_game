package com.solo4.millionerquiz.data.di

import com.google.gson.Gson
import com.solo4.millionerquiz.data.database.deserializers.FirebaseDBDataDeserializer
import org.koin.dsl.module

val utilsModule = module {
    single { Gson() }
    factory { FirebaseDBDataDeserializer(get()) }
}