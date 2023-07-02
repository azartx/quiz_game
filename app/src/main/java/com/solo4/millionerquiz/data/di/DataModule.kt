package com.solo4.millionerquiz.data.di

import com.solo4.millionerquiz.data.auth.AuthManager
import org.koin.dsl.module

val dataModule = module {
    single { AuthManager() }
}
