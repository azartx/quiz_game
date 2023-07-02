package com.solo4.millionerquiz

import android.app.Application
import com.solo4.millionerquiz.data.di.dataModule
import com.solo4.millionerquiz.data.di.repositoryModule
import com.solo4.millionerquiz.data.di.viewModelModule
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(listOf(viewModelModule, repositoryModule, dataModule))
        }
    }
}
