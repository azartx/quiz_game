package com.solo4.millionerquiz

import android.app.Application
import com.solo4.millionerquiz.data.di.dataModule
import com.solo4.millionerquiz.data.di.repositoryModule
import com.solo4.millionerquiz.data.di.utilsModule
import com.solo4.millionerquiz.data.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(viewModelModule, repositoryModule, dataModule, utilsModule))
        }
    }
}
