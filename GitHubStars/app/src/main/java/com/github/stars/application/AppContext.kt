package com.github.stars.application

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.github.stars.application.AppModule.searchModule
import com.github.stars.application.AppModule.servicesModule
import org.koin.android.ext.android.startKoin


class AppContext : Application() {
    companion object {
        lateinit var instance: AppContext
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin()
    }

    private fun startKoin() {
        startKoin(
            this,
            listOf(
                servicesModule,
                searchModule
            )
        )
    }
}
