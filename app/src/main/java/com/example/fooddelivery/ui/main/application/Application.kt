package com.example.fooddelivery.ui.main.application

import android.app.Application
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import com.example.domain.di.interactionModule
import com.example.data.di.databaseModule
import com.example.data.di.networkingModule
import com.example.data.di.repositoryModule
import com.example.fooddelivery.ui.main.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

import org.koin.core.logger.Level

class Application : Application() {

    private val appModules = listOf(presentationModule)
    private val domainModules = listOf(interactionModule)
    private val dataModules = listOf(networkingModule, repositoryModule, databaseModule)
    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin {
            androidContext(this@Application)
            androidLogger(Level.DEBUG)
            modules(appModules + domainModules + dataModules)
        }
    }


    companion object {
        lateinit var instance: Application
            private set
        private val TAG = Application::class.java.simpleName
    }
}