package com.example.flickrtestapp

import android.app.Application
import com.example.flickrtestapp.di.koin.modules.koinApiModule
import com.example.flickrtestapp.di.koin.modules.koinAppModule
import com.facebook.stetho.Stetho
import com.example.flickrtestapp.di.koin.modules.koinNetworkModule
import com.example.flickrtestapp.di.koin.modules.koinPresenterModule
import com.example.flickrtestapp.di.koin.modules.koinRetrofitModule
import com.example.flickrtestapp.di.koin.modules.koinRxModule
import com.example.flickrtestapp.util.FlickrTypeMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class TestApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        startKoin()
        FlickrTypeMapper.initConverters()
    }

    private fun startKoin() {
        startKoin {
            androidContext(this@TestApplication.applicationContext)
            modules(
                listOf(
                    koinApiModule,
                    koinAppModule,
                    koinNetworkModule,
                    koinRetrofitModule,
                    koinRxModule,
                    koinPresenterModule
                )
            )
        }
    }
}